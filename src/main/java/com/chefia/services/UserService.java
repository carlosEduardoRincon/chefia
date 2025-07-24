package com.chefia.services;

import com.chefia.entities.User;
import com.chefia.mapper.AddressMapper;
import com.chefia.mapper.UserMapper;
import com.chefia.dtos.UserResponseDTO;
import com.chefia.repositories.UserRepository;
import com.chefia.services.exceptions.UserNotFoundException;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.PaginatedUsersDTO;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public UserDTO saveUser(CreateUserDTO createUserDTO) {
        var userToInsert = this.userMapper.toEntity(createUserDTO);
        this.userRepository.save(userToInsert);
        return this.userMapper.toResponseDTO(userToInsert);
    }

    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        var userEntity = this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userEntity.setName(updateUserDTO.getName());
        userEntity.setAddress(this.addressMapper.toEntityList(updateUserDTO.getAddress()));
        return this.userMapper.toResponseDTO(this.userRepository.save(userEntity));
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
}
