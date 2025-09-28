package com.chefia.core.service;

import com.chefia.core.port.input.user.UserInputPort;
import com.chefia.core.port.output.address.AddressRepositoryOutputPort;
import com.chefia.core.port.output.user.UserRepositoryOutputPort;
import com.chefia.core.port.output.user.UserValidatorOutputPort;
import com.chefia.core.port.output.usertype.UserTypeRepositoryOutputPort;
import com.chefia.domain.model.User;
import com.chefia.infra.exception.*;
import com.chefia.infra.mapper.UserMapper;
import com.chefia.users.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.chefia.infra.validation.StrongPasswordValidator.isValid;

@Service
public class UserService implements UserInputPort {

    private final UserRepositoryOutputPort userRepositoryOutputPort;

    private final List<UserValidatorOutputPort> userValidatorOutputPorts;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryOutputPort userRepositoryOutputPort,
                       List<UserValidatorOutputPort> userValidatorOutputPorts,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder
    ) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
        this.userValidatorOutputPorts = userValidatorOutputPorts;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO saveUser(CreateUserDTO createUserDTO) {
        var userToInsert = this.userMapper.toEntity(createUserDTO);

        this.validateUser(userToInsert);

        var userId = this.userRepositoryOutputPort.save(userToInsert);
        userToInsert.setNrSeqUser(userId);

        return this.userMapper.toUserResponseDTO(userToInsert);
    }

    private void validateUser(User user) {
        for (UserValidatorOutputPort userValidator : userValidatorOutputPorts) {
            userValidator.validate(user);
        }
    }

    public UserDTO findById(Long userId) {
        var user = Optional.ofNullable(this.userRepositoryOutputPort
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();
        return this.userMapper.toUserResponseDTO(user.get());
    }

    public PaginatedUsersDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<User> userPage = this.userRepositoryOutputPort.findAll(pageable);

        var userDTOs = this.userMapper.toResponseListDTO(userPage);

        return new PaginatedUsersDTO()
                .page(page)
                .perPage(perPage)
                .total((long) userPage.size())
                .items(userDTOs);
    }

    public UserDTO updateUser(Long userId, UpdateUserDTO body) {
        var userEntity = this.userRepositoryOutputPort
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userEntity.setName(body.getName());
        userEntity.setEmail(body.getEmail());
        userEntity.setLogin(body.getLogin());
        userEntity.setUpdatedAt(LocalDateTime.now());

        this.userRepositoryOutputPort.update(userId, userEntity);

        return this.userMapper.toUserResponseDTO(userEntity);
    }

    public void deleteUser(Long userId) {
        this.userRepositoryOutputPort.deleteById(userId);
    }

    public void changeUserStatus(Long userId, Boolean status) {
        var userEntity = this.userRepositoryOutputPort
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userEntity.setActive(status);
        userEntity.updatedAt();

        this.userRepositoryOutputPort.updateUserStatus(userId, userEntity);
    }

    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        var userEntity = this.userRepositoryOutputPort
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        boolean passwordMatches = this.passwordEncoder.matches(changePasswordDTO.getOldPassword(), userEntity.getPassword());
        boolean newPasswordIsEqualToOld = changePasswordDTO.getOldPassword().equals(changePasswordDTO.getNewPassword());

        if (!passwordMatches) {
            throw new PasswordNotMatch("Wrong password");
        } else if (newPasswordIsEqualToOld) {
            throw new PasswordAlreadyUsed("The passwords are equals");
        } else if (!isValid(changePasswordDTO.getNewPassword())) {
            throw new UserNotStrongPassword("New password not strong");
        }

        this.userRepositoryOutputPort.updateUserPassword(userId, userEntity);
    }
}
