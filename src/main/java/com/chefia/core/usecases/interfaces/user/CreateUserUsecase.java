package com.chefia.core.usecases.interfaces.user;

import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;

public interface CreateUserUsecase {
    UserDTO execute(CreateUserDTO createUserDTO);
}
