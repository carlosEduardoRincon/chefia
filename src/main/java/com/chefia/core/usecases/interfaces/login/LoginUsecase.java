package com.chefia.core.usecases.interfaces.login;

import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;

public interface LoginUsecase {
    LoginResponseDTO execute(LoginUserDTO loginUserDTO);
}
