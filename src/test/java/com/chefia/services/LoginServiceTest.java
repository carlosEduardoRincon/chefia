package com.chefia.services;

import com.chefia.entities.User;
import com.chefia.infra.security.TokenService;
import com.chefia.mapper.LoginMapper;

import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private LoginMapper loginMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoginSuccessfully() {
        var login = "user";
        var password = "pass";
        var jwt = "jwt-token";

        var loginDTO = new LoginUserDTO();
        loginDTO.setLogin(login);
        loginDTO.setPassword(password);

        var user = new User();
        var authentication = mock(Authentication.class);
        var expectedResponse = new LoginResponseDTO();

        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(user);
        when(this.tokenService.generateJWT(user)).thenReturn(jwt);
        when(this.loginMapper.toResponseDTO(jwt)).thenReturn(expectedResponse);

        var result = this.loginService.login(loginDTO);

        assertEquals(expectedResponse, result);
        verify(this.authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(this.tokenService).generateJWT(user);
        verify(this.loginMapper).toResponseDTO(jwt);
    }
}
