package com.chefia.adapters.login;

import com.chefia.core.port.input.LoginInputPort;
import com.chefia.users.api.LoginApi;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController implements LoginApi {

    private final LoginInputPort loginInputPort;

    public LoginController(LoginInputPort loginInputPort) {
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
