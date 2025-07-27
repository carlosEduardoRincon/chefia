package com.chefia.services;

import com.chefia.entities.User;
import com.chefia.mapper.AddressMapper;
import com.chefia.mapper.UserMapper;
import com.chefia.repositories.UserRepository;
import com.chefia.services.exceptions.UserNotFoundException;
import com.chefia.users.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            AddressMapper addressMapper

    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }

    public UserDTO saveUser(CreateUserDTO createUserDTO) {
        // Adicionar classe de validação: validar e-mail existente
        var userToInsert = this.userMapper.toEntity(createUserDTO);
        for (var address : createUserDTO.getAddress()) {
            var mappedAddress = this.addressMapper.toCreateAddressEntity(address);
            mappedAddress.setUser(userToInsert);
            userToInsert.getAddress().add(mappedAddress);
        }

        this.userRepository.save(userToInsert);
        return this.userMapper.toResponseDTO(userToInsert);
    }

    public UserDTO findById(Long id) {
        var user = Optional.ofNullable(this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
        assert user.isPresent();
        return this.userMapper.toResponseDTO(user.get());
    }

    public PaginatedUsersDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<User> userPage = userRepository.findAll(pageable);

        var userDTOs = userMapper.toResponseListDTO(userPage.getContent());

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
        return userMapper.toResponseDTO(userEntity);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public void changeUserStatus(Long id, Boolean status) {
        var userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userEntity.setActive(status);
        userRepository.flush();
    }
}
