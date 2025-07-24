package com.chefia.mapper;

import com.chefia.dtos.UserResponseDTO;
import com.chefia.entities.User;
import com.chefia.entities.enums.ProfileType;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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

    public UserDTO toResponseDTO(User user) {
        var userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setAddress(this.addressMapper.toDTOList(user.getAddress()));
        userDTO.setActive(user.isActive());
        userDTO.setCreatedAt(OffsetDateTime.from(user.getCreatedAt()));
        userDTO.setUpdatedAt(OffsetDateTime.from(user.getUpdatedAt()));
        userDTO.setDeletedAt(OffsetDateTime.from(user.getDeletedAt()));
        return userDTO;
    }

    public List<UserDTO> toResponseListDTO(List<User> userList) {
        var usersResponse = new ArrayList<UserDTO>();
        for (var user : userList) {
            usersResponse.add(this.toResponseDTO(user));
        }
        return usersResponse;
    }
}
