package com.chefia.mapper;

import com.chefia.entities.User;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
}
