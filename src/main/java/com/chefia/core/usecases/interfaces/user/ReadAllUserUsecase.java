package com.chefia.core.usecases.interfaces.user;

import com.chefia.users.model.PaginatedUsersDTO;

public interface ReadAllUserUsecase {
    PaginatedUsersDTO execute(Integer page, Integer perPage);
}
