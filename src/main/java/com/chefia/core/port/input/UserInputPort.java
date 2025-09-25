package com.chefia.core.port.input;

import com.chefia.users.model.*;

public interface UserInputPort {
    UserDTO saveUser(CreateUserDTO createUserDTO);

    UserDTO findById(Long userId);

    PaginatedUsersDTO findAll(Integer page, Integer perPage);

    UserDTO updateUser(Long userId, UpdateUserDTO body);

    void deleteUser(Long userId);

    void changeUserStatus(Long userId, Boolean aTrue);

    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
}
