package com.chefia.mapper;

import com.chefia.entities.User;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import com.chefia.validation.annotation.StrongPassword;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;

    public UserMapper(PasswordEncoder passwordEncoder, AddressMapper addressMapper) {
        this.passwordEncoder = passwordEncoder;
        this.addressMapper = addressMapper;
    }

    public User toEntity(CreateUserDTO createUserDTO) {
        return new User(createUserDTO.getName(),
                createUserDTO.getEmail(),
                createUserDTO.getLogin(),
                passwordEncoder.encode(createUserDTO.getPassword()),
                Boolean.TRUE,
                LocalDateTime.now(),
                createUserDTO.getProfileType().name());
    }

    public UserDTO toResponseDTO(User user) {
        var userDTO = new UserDTO();

        userDTO.setId(user.getNrSeqUser());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setAddress(this.addressMapper.toDTOList(user.getAddress()));
        userDTO.setActive(user.isActive());
        userDTO.setCreatedAt(user.getCreatedAt().atOffset(ZoneOffset.ofHours(-3)));
        userDTO.setUpdatedAt(user.getUpdatedAt() != null? user.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3)) : null);

        return userDTO;
    }

    public List<UserDTO> toResponseListDTO(List<User> userList) {
        var usersResponse = new ArrayList<UserDTO>();
        for (var user : userList) {
            usersResponse.add(this.toResponseDTO(user));
        }
        return usersResponse;
    }

    public User toUpdateEntity(User userEntity, UpdateUserDTO updateUserDTO) {
        return new User(userEntity.getNrSeqUser(),
                updateUserDTO.getName(),
                updateUserDTO.getEmail(),
                updateUserDTO.getLogin(),
                userEntity.getPassword(),
                Boolean.TRUE,
                userEntity.getCreatedAt(),
                LocalDateTime.now(),
                userEntity.getProfileType(),
                userEntity.getAddress()
        );
    }

    public User toUpdatePasswordEntity(User user, String newPassword) {
        return new User(user.getNrSeqUser(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                newPassword,
                user.isActive(),
                user.getCreatedAt(),
                LocalDateTime.now(),
                user.getProfileType(),
                user.getAddress()
        );
    }
}
