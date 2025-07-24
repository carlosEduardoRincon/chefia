package com.chefia.services;

import com.chefia.mapper.UserMapper;
import com.chefia.dtos.UserResponseDTO;
import com.chefia.repositories.UserRepository;
import com.chefia.services.exceptions.UserNotFoundException;
import com.chefia.users.model.CreateUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO findById(Long id) {
        var user = Optional.ofNullable(this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
        assert user.isPresent();
        return this.userMapper.toResponseDTO(user.get());
    }

    public List<UserResponseDTO> findAll() {
        return this.userMapper.toResponseListDTO(this.userRepository.findAll());
    }

    public UserResponseDTO saveUser(CreateUserDTO createUserDTO) {
        var userToInsert = this.userMapper.toEntity(createUserDTO);
        this.userRepository.save(userToInsert);
        return this.userMapper.toResponseDTO(userToInsert);
    }
}
