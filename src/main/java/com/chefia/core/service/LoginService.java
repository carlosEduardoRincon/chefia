package com.chefia.core.service;

import com.chefia.core.port.input.LoginInputPort;
import com.chefia.domain.model.User;
import com.chefia.infra.security.TokenService;
import com.chefia.infra.mapper.LoginMapper;
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