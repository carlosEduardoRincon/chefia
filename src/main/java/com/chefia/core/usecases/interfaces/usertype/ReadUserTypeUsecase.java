package com.chefia.core.usecases.interfaces.usertype;

import com.chefia.usertypes.model.UserTypeDTO;

public interface ReadUserTypeUsecase {
    UserTypeDTO execute(Long userTypeId);
}
