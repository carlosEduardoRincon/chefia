package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.usertype.*;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserTypeController {

    private final CreateUserTypeUsecase createUserTypeUsecase;
    private final ReadUserTypeUsecase readUserTypeUsecase;
    private final ReadAllUserTypeUsecase readAllUserTypeUsecase;
    private final UpdateUserTypeUsecase updateUserTypeUsecase;
    private final DeleteUserTypeUsecase deleteUserTypeUsecase;

    public UserTypeDTO saveUserType(CreateUserTypeDTO createUserTypeDTO)
    {
        return this.createUserTypeUsecase.execute(createUserTypeDTO);
    }

    public UserTypeDTO findById(Long userTypeId)
    {
        return this.readUserTypeUsecase.execute(userTypeId);
    }

    public PaginatedUserTypeDTO findAll(Integer page, Integer perPage)
    {
        return this.readAllUserTypeUsecase.execute(page, perPage);
    }

    public UserTypeDTO updateUserType(Long userTypeId, UpdateUserTypeDTO updateUserTypeDTO)
    {
        return this.updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);
    }

    public void deleteUserType(Long userTypeId)
    {
        this.deleteUserTypeUsecase.execute(userTypeId);
    }
}
