package com.chefia.core.usecases.impl.login;

import com.chefia.core.entities.User;
import com.chefia.core.mapper.LoginMapper;
import com.chefia.core.usecases.interfaces.login.LoginUsecase;
import com.chefia.infra.config.security.TokenService;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class LoginUsecaseImpl implements LoginUsecase {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LoginMapper loginMapper;

    @Override
    public LoginResponseDTO execute(LoginUserDTO loginUserDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO.getLogin(), loginUserDTO.getPassword());
        var authentication = this.authenticationManager.authenticate(authenticationToken);
        var tokenJWT = this.tokenService.generateJWT((User) authentication.getPrincipal());
        return this.loginMapper.toUserResponseDTO(tokenJWT);
    }
}
