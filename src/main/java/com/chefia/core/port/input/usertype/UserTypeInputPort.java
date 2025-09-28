package com.chefia.core.port.input.usertype;

import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;

public interface UserTypeInputPort {
    UserTypeDTO saveUserType(CreateUserTypeDTO body);

    UserTypeDTO findById(Long userTypeId);

    PaginatedUserTypeDTO findAll(Integer page, Integer perPage);

    UserTypeDTO updateUserType(Long userTypeId, UpdateUserTypeDTO body);

    void deleteUserType(Long userTypeId);
}
