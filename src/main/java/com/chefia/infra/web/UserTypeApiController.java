package com.chefia.infra.web;

import com.chefia.core.controllers.UserTypeController;
import com.chefia.usertypes.api.UsertypeApi;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class UserTypeApiController implements UsertypeApi {

    private final UserTypeController userTypeController;

    @Override
    public ResponseEntity<UserTypeDTO> createUserType(CreateUserTypeDTO body)
    {
        log.info("[POST] - Create UserType");
        var createdUser = this.userTypeController.saveUserType(body);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Override
    public ResponseEntity<UserTypeDTO> getUserType(Long userTypeId)
    {
        log.info("[GET] - List UserType");
        var getUser = this.userTypeController.findById(userTypeId);
        return ResponseEntity.ok(getUser);
    }

    @Override
    public ResponseEntity<PaginatedUserTypeDTO> listUserTypes(Integer page, Integer perPage)
    {
        log.info("[GET] - List All UserType");
        var listAllUsers = this.userTypeController.findAll(page, perPage);
        return ResponseEntity.ok(listAllUsers);
    }

    @Override
    public ResponseEntity<UserTypeDTO> updateUserType(Long userTypeId, UpdateUserTypeDTO body)
    {
        log.info("[PUT] - Update UserType");
        var updatedUser = this.userTypeController.updateUserType(userTypeId, body);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUserType(Long userTypeId)
    {
        log.info("[DELETE] - Remove UserType");
        this.userTypeController.deleteUserType(userTypeId);
        return ResponseEntity.noContent().build();
    }
}
