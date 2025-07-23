package com.chefia.controllers;

import com.chefia.users.api.UserApi;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.PaginatedUsersDTO;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController implements UserApi {

    @Override
    public ResponseEntity<UserDTO> updateUser(UUID userId, UpdateUserDTO body) {
        return UserApi.super.updateUser(userId, body);
    }

    @Override
    public ResponseEntity<PaginatedUsersDTO> listUsers(Integer page, Integer perPage) {
        return UserApi.super.listUsers(page, perPage);
    }

    @Override
    public ResponseEntity<UserDTO> getUser(UUID userId) {
        return UserApi.super.getUser(userId);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        return UserApi.super.deleteUser(userId);
    }

    @Override
    public ResponseEntity<UserDTO> createUser(CreateUserDTO body) {
        return UserApi.super.createUser(body);
    }
}
