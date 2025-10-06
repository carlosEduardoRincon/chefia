package com.chefia.core.usecases.interfaces.usertype;

import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;

public interface UpdateUserTypeUsecase {
    UserTypeDTO execute(Long userTypeId, UpdateUserTypeDTO body);
}
