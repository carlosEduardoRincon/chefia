package com.chefia.core.usecases.interfaces.usertype;

import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;

public interface CreateUserTypeUsecase {
    UserTypeDTO execute(CreateUserTypeDTO body);
}
