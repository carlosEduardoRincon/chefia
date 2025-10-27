package com.chefia.core.usecases.interfaces.user;

import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;

public interface UpdateUserUsecase {
    UserDTO execute(Long userId, UpdateUserDTO body);
}
