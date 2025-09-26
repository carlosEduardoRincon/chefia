package com.chefia.adapters.usertype.inputs;

import com.chefia.core.port.input.UserTypeInputPort;
import com.chefia.usertypes.api.UserTypeApi;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping
public class UserTypeController implements UserTypeApi {

    private final UserTypeInputPort userTypeInputPort;

    public UserTypeController(UserTypeInputPort userTypeInputPort) {
        this.userTypeInputPort = userTypeInputPort;
    }

    @Override
    public ResponseEntity<UserTypeDTO> createUserType(CreateUserTypeDTO body)
    {
        log.info("[POST] - Create UserType");
        var createdUser = this.userTypeInputPort.saveUserType(body);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Override
    public ResponseEntity<UserTypeDTO> getUserType(Long userTypeId)
    {
        log.info("[GET] - List UserType");
        var getUser = this.userTypeInputPort.findById(userTypeId);
        return ResponseEntity.ok(getUser);
    }

    @Override
    public ResponseEntity<PaginatedUserTypeDTO> listUserTypes(Integer page, Integer perPage)
    {
        log.info("[GET] - List All UserType");
        var listAllUsers = this.userTypeInputPort.findAll(page, perPage);
        return ResponseEntity.ok(listAllUsers);
    }

    @Override
    public ResponseEntity<UserTypeDTO> updateUserType(Long userTypeId, UpdateUserTypeDTO body)
    {
        log.info("[PUT] - Update UserType");
        var updatedUser = this.userTypeInputPort.updateUserType(userTypeId, body);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUserType(Long userTypeId)
    {
        log.info("[DELETE] - Remove UserType");
        this.userTypeInputPort.deleteUserType(userTypeId);
        return ResponseEntity.noContent().build();
    }
}
