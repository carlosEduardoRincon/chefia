package com.chefia.mapper;

import com.chefia.dtos.UserResponseDTO;
import com.chefia.entities.User;
import com.chefia.entities.enums.ProfileType;
import com.chefia.users.model.CreateUserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final AddressMapper addressMapper;

    public UserMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public User toEntity(CreateUserDTO createUserDTO) {
        return new User(createUserDTO.getName(),
                createUserDTO.getEmail(),
                createUserDTO.getLogin(),
                createUserDTO.getPassword(),
                this.addressMapper.toEntityList(createUserDTO.getAddress()),
                Boolean.TRUE,
                LocalDate.now(),
                ProfileType.valueOf(createUserDTO.getProfileType().name()));
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getAddress(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt(),
                user.getProfileType()
        );
    }

    public List<UserResponseDTO> toResponseListDTO(List<User> userList) {
        var usersResponse = new ArrayList<UserResponseDTO>();
        for (var user : userList) {
            usersResponse.add(toResponseDTO(user));
        }
        return usersResponse;
    }
}
