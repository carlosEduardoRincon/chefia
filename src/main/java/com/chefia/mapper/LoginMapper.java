package com.chefia.mapper;

import com.chefia.users.model.LoginResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public LoginResponseDTO toResponseDTO(String token) {
        var loginResponseDTO = new LoginResponseDTO();

        loginResponseDTO.setToken(token);

        return loginResponseDTO;
    }
}
