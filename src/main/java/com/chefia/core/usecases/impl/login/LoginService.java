package com.chefia.core.usecases.impl.login;

import com.chefia.core.usecases.interfaces.login.LoginInputPort;
import com.chefia.core.entities.User;
import com.chefia.infra.config.security.TokenService;
import com.chefia.core.mapper.LoginMapper;
import com.chefia.users.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService implements LoginInputPort {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LoginMapper loginMapper;

    public LoginService(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            LoginMapper loginMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.loginMapper = loginMapper;
    }

    public LoginResponseDTO login(LoginUserDTO loginDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword());
        var authentication = this.authenticationManager.authenticate(authenticationToken);
        var tokenJWT = this.tokenService.generateJWT((User) authentication.getPrincipal());
        return this.loginMapper.toUserResponseDTO(tokenJWT);
    }
}