package com.chefia.services;

import com.chefia.entities.User;
import com.chefia.infra.security.TokenService;
import com.chefia.mapper.LoginMapper;
import com.chefia.users.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {
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
        return this.loginMapper.toResponseDTO(tokenJWT);
    }
}