package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.user.*;
import com.chefia.users.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserController {

    private final CreateUserUsecase createUserUsecase;
    private final ReadAllUserUsecase readAllUserUsecase;
    private final ReadUserUsecase readUserUsecase;
    private final UpdateUserUsecase updateUserUsecase;
    private final UpdateUserStatusUsecase updateUserStatusUsecase;
    private final UpdateUserPasswordUsecase updateUserPasswordUsecase;
    private final DeleteUserUsecase deleteUserUsecase;

    public UserDTO saveUser(CreateUserDTO createUserDTO)
    {
        return this.createUserUsecase.execute(createUserDTO);
    }

    public UserDTO findById(Long userId)
    {
        return this.readUserUsecase.execute(userId);
    }

    public PaginatedUsersDTO findAll(Integer page, Integer perPage)
    {
        return this.readAllUserUsecase.execute(page, perPage);
    }

    public UserDTO updateUser(Long userId, UpdateUserDTO updateUserDTO)
    {
        return this.updateUserUsecase.execute(userId, updateUserDTO);
    }

    public void changeUserStatus(Long userId, Boolean status)
    {
        this.updateUserStatusUsecase.execute(userId, status);
    }

    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO)
    {
        this.updateUserPasswordUsecase.execute(userId, changePasswordDTO);
    }

    public void deleteUser(Long userId)
    {
        this.deleteUserUsecase.execute(userId);
    }
}
