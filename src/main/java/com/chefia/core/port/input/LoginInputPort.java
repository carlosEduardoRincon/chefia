package com.chefia.core.port.input;

import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;

public interface LoginInputPort {

    LoginResponseDTO login(LoginUserDTO loginUserDTO);
}
