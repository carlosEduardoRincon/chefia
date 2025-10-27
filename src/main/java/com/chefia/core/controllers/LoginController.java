package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.login.LoginUsecase;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class LoginController {

    private final LoginUsecase loginUsecase;

    public LoginResponseDTO login(LoginUserDTO loginUserDTO)
    {
        return this.loginUsecase.execute(loginUserDTO);
    }
}
