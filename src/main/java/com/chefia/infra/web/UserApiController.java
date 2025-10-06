package com.chefia.infra.web;

import com.chefia.core.controllers.UserController;
import com.chefia.users.api.UserApi;
import com.chefia.users.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class UserApiController implements UserApi {

    private final UserController userController;

    @Override
    public ResponseEntity<UserDTO> createUser(CreateUserDTO createUserDTO)
    {
        log.info("[POST] - Create User");
        var createdUser = this.userController.saveUser(createUserDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Override
    public ResponseEntity<UserDTO> getUser(Long userId)
    {
        log.info("[GET] - List User");
        var getUser = this.userController.findById(userId);
        return ResponseEntity.ok(getUser);
    }

    @Override
    public ResponseEntity<PaginatedUsersDTO> listUsers(Integer page, Integer perPage)
    {
        log.info("[GET] - List All Users");
        var listAllUsers = this.userController.findAll(page, perPage);
        return ResponseEntity.ok(listAllUsers);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Long userId, UpdateUserDTO body)
    {
        log.info("[PUT] - Update User");
        var updatedUser = this.userController.updateUser(userId, body);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId)
    {
        log.info("[DELETE] - Remove User");
        this.userController.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> enableUser(Long userId)
    {
        log.info("[PATCH] - Enable User");
        this.userController.changeUserStatus(userId, Boolean.TRUE);
        var status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).build();
    }

    @Override
    public ResponseEntity<Void> disableUser(Long userId)
    {
        log.info("[PATCH] - Disable User");
        this.userController.changeUserStatus(userId, Boolean.FALSE);
        var status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).build();
    }

    @Override
    public ResponseEntity<Void> changePassword(Long userId, ChangePasswordDTO changePasswordDTO)
    {
        log.info("[PATCH] - Change Password");
        this.userController.changePassword(userId, changePasswordDTO);
        var status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).build();
    }
}
