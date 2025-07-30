package com.chefia.controllers;

import com.chefia.services.UserService;
import com.chefia.users.api.UserApi;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.PaginatedUsersDTO;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDTO> createUser(CreateUserDTO createUserDTO)
    {
        log.info("[POST] - Create User");
        var createdUser = this.userService.saveUser(createUserDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Override
    public ResponseEntity<UserDTO> getUser(Long userId)
    {
        log.info("[GET] - List User");
        var getUser = this.userService.findById(userId);
        return ResponseEntity.ok(getUser);
    }

    @Override
    public ResponseEntity<PaginatedUsersDTO> listUsers(Integer page, Integer perPage)
    {
        log.info("[GET] - List All Users");
        var listAllUsers = this.userService.findAll(page, perPage);
        return ResponseEntity.ok(listAllUsers);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Long userId, UpdateUserDTO body)
    {
        log.info("[PUT] - Update User");
        var updatedUser = this.userService.updateUser(userId, body);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId)
    {
        log.info("[DELETE] - Remove User");
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> enableUser(Long userId)
    {
        log.info("[PATCH] - Enable User");
        this.userService.changeUserStatus(userId, Boolean.TRUE);
        var status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).build();
    }

    @Override
    public ResponseEntity<Void> disableUser(Long userId)
    {
        log.info("[PATCH] - Disable User");
        this.userService.changeUserStatus(userId, Boolean.FALSE);
        var status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).build();
    }
}
