package com.chefia.core.usecases.interfaces.user;

import com.chefia.users.model.UserDTO;

public interface ReadUserUsecase {
    UserDTO execute(Long userId);
}
