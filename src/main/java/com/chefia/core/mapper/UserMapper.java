package com.chefia.core.mapper;

import com.chefia.core.entities.User;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(CreateUserDTO createUserDTO) {
        return new User(createUserDTO.getName(),
                createUserDTO.getEmail(),
                createUserDTO.getLogin(),
                passwordEncoder.encode(createUserDTO.getPassword()),
                Boolean.TRUE,
                LocalDateTime.now(),
                createUserDTO.getUserTypeId()
        );
    }

    public UserDTO toUserResponseDTO(User user) {
        return new UserDTO()
                .id(user.getNrSeqUser())
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .active(user.isActive())
                .createdAt(user.getCreatedAt().atOffset(ZoneOffset.ofHours(-3)))
                .updatedAt(user.getUpdatedAt() != null? user.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3)) : null)
                .userTypeId(user.getUserTypeId());
    }

    public List<UserDTO> toResponseListDTO(List<User> userList) {
        var usersResponse = new ArrayList<UserDTO>();
        for (var user : userList) {
            usersResponse.add(this.toUserResponseDTO(user));
        }
        return usersResponse;
    }
}
