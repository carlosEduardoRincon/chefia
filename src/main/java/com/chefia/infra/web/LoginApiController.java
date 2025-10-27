package com.chefia.infra.web;

import com.chefia.core.controllers.LoginController;
import com.chefia.users.api.LoginApi;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class LoginApiController implements LoginApi {

    private final LoginController loginController;

    @Override
    public ResponseEntity<LoginResponseDTO> login(LoginUserDTO loginUserDTO)
    {
        log.info("[POST] - Login");
        var loginResponse = this.loginController.login(loginUserDTO);
        return ResponseEntity.ok().body(loginResponse);
    }
}
