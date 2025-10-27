package com.chefia.core.usecases.interfaces.usertype;

import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;

public interface ReadAllUserTypeUsecase {
    PaginatedUserTypeDTO execute(Integer page, Integer perPage);
}
