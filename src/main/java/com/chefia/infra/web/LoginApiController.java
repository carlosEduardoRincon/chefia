package com.chefia.infra.web;

import com.chefia.core.usecases.interfaces.login.LoginInputPort;
import com.chefia.users.api.LoginApi;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginApiController implements LoginApi {

    private final LoginInputPort loginInputPort;

    public LoginApiController(LoginInputPort loginInputPort) {
        this.loginInputPort = loginInputPort;
    }

    @Override
    public ResponseEntity<LoginResponseDTO> login(LoginUserDTO loginUserDTO)
    {
        log.info("[POST] - Login");
        var loginResponse = this.loginInputPort.login(loginUserDTO);
        return ResponseEntity.ok().body(loginResponse);
    }
}
