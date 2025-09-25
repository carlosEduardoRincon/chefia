package com.chefia.core.service;

import com.chefia.domain.model.User;
import com.chefia.infra.exception.PasswordAlreadyUsed;
import com.chefia.infra.exception.PasswordNotMatch;
import com.chefia.infra.exception.UserNotStrongPassword;
import com.chefia.infra.mapper.AddressMapper;
import com.chefia.infra.mapper.UserMapper;
import com.chefia.core.port.output.user.UserRepository;
import com.chefia.infra.exception.UserNotFoundException;
import com.chefia.users.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.chefia.infra.validation.StrongPasswordValidator.isValid;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            AddressMapper addressMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO saveUser(CreateUserDTO createUserDTO) {
        var userToInsert = this.userMapper.toEntity(createUserDTO);
        handleUserAddress(createUserDTO, userToInsert);

        this.userRepository.save(userToInsert);
        return this.userMapper.toUserResponseDTO(userToInsert);
    }

    public UserDTO findById(Long id) {
        var user = Optional.ofNullable(this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
        assert user.isPresent();
        return this.userMapper.toUserResponseDTO(user.get());
    }

    public PaginatedUsersDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<User> userPage = this.userRepository.findAll(pageable);

        var userDTOs = this.userMapper.toResponseListDTO(userPage.getContent());

        return new PaginatedUsersDTO()
                .page(page)
                .perPage(perPage)
                .total(userPage.getTotalElements())
                .items(userDTOs);
    }

    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        var userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userEntity.setName(updateUserDTO.getName());
        userEntity.setEmail(updateUserDTO.getEmail());
        userEntity.setLogin(updateUserDTO.getLogin());
        userEntity.setUpdatedAt(LocalDateTime.now());

        userRepository.flush();
        return userMapper.toUserResponseDTO(userEntity);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public void changeUserStatus(Long id, Boolean status) {
        var userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userEntity.setActive(status);
        userEntity.updatedAt();
        userRepository.flush();
    }

    private void handleUserAddress(CreateUserDTO createUserDTO, User userToInsert) {
        for (var address : createUserDTO.getAddress()) {
            var mappedAddress = this.addressMapper.toCreateAddressEntity(address);
            mappedAddress.setUser(userToInsert);
            userToInsert.getAddress().add(mappedAddress);
        }
    }

    public void changePassword(Long id, ChangePasswordDTO changePasswordDTO) {
        var userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        boolean passwordMatches = this.passwordEncoder.matches(changePasswordDTO.getOldPassword(), userEntity.getPassword());
        boolean newPasswordIsEqualToOld = changePasswordDTO.getOldPassword().equals(changePasswordDTO.getNewPassword());

        if (!passwordMatches) {
            throw new PasswordNotMatch("Wrong password");
        } else if (newPasswordIsEqualToOld) {
            throw new PasswordAlreadyUsed("The passwords are equals");
        } else if (!isValid(changePasswordDTO.getNewPassword())) {
            throw new UserNotStrongPassword("New password not strong");
        }

        userEntity = this.userMapper.toUpdatePasswordEntity(userEntity, this.passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        this.userRepository.save(userEntity);
        this.userRepository.flush();
    }
}
