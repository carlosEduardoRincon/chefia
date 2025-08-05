package com.chefia.controllers;

import com.chefia.services.LoginService;
import com.chefia.users.api.LoginApi;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController implements LoginApi {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public ResponseEntity<LoginResponseDTO> login(LoginUserDTO loginUserDTO)
    {
        log.info("[POST] - Login");
        var loginResponse = this.loginService.login(loginUserDTO);
        return ResponseEntity.ok().body(loginResponse);
    }
}
