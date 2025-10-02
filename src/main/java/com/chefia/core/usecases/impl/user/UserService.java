package com.chefia.core.usecases.impl.user;

import com.chefia.core.exceptions.PasswordAlreadyUsed;
import com.chefia.core.exceptions.PasswordNotMatch;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.exceptions.UserNotStrongPassword;
import com.chefia.core.usecases.interfaces.user.UserInputPort;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.gateway.UserValidatorGateway;
import com.chefia.core.entities.User;
import com.chefia.core.mapper.UserMapper;
import com.chefia.users.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.chefia.infra.validation.annotation.StrongPasswordValidator.isValid;

@Service
public class UserService implements UserInputPort {

    private final UserGateway userGateway;
    private final List<UserValidatorGateway> userValidatorGateways;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserGateway userGateway,
                       List<UserValidatorGateway> userValidatorGateways,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder
    ) {
        this.userGateway = userGateway;
        this.userValidatorGateways = userValidatorGateways;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO saveUser(CreateUserDTO createUserDTO) {
        var userToInsert = this.userMapper.toEntity(createUserDTO);

        this.validateUser(userToInsert);

        var userId = this.userGateway.save(userToInsert);
        userToInsert.setNrSeqUser(userId);

        return this.userMapper.toUserResponseDTO(userToInsert);
    }

    private void validateUser(User user) {
        for (UserValidatorGateway userValidator : userValidatorGateways) {
            userValidator.validate(user);
        }
    }

    public UserDTO findById(Long userId) {
        var user = Optional.ofNullable(this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();
        return this.userMapper.toUserResponseDTO(user.get());
    }

    public PaginatedUsersDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<User> userPage = this.userGateway.findAll(pageable);

        var userDTOs = this.userMapper.toResponseListDTO(userPage);

        return new PaginatedUsersDTO()
                .page(page)
                .perPage(perPage)
                .total((long) userPage.size())
                .items(userDTOs);
    }

    public UserDTO updateUser(Long userId, UpdateUserDTO body) {
        var userEntity = this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userEntity.setName(body.getName());
        userEntity.setEmail(body.getEmail());
        userEntity.setLogin(body.getLogin());
        userEntity.setUpdatedAt(LocalDateTime.now());

        this.userGateway.update(userId, userEntity);

        return this.userMapper.toUserResponseDTO(userEntity);
    }

    public void deleteUser(Long userId) {
        this.userGateway.deleteById(userId);
    }

    public void changeUserStatus(Long userId, Boolean status) {
        var userEntity = this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userEntity.setActive(status);
        userEntity.updatedAt();

        this.userGateway.updateUserStatus(userId, userEntity);
    }

    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        var userEntity = this.userGateway
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

        this.userGateway.updateUserPassword(userId, userEntity);
    }
}
