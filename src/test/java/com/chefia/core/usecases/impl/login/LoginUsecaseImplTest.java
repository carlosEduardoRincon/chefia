package com.chefia.core.usecases.impl.login;

import com.chefia.core.entities.User;
import com.chefia.core.mapper.LoginMapper;
import com.chefia.infra.config.security.TokenService;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUsecaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private LoginMapper loginMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginUsecaseImpl loginUsecase;

    private LoginUserDTO loginUserDTO;
    private User user;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        loginUserDTO = new LoginUserDTO();
        loginUserDTO.setLogin("john@example.com");
        loginUserDTO.setPassword("password123");

        user = new User();
        user.setNrSeqUser(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");
        user.setActive(true);

        loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken("jwt-token-123");
    }

    @Test
    void execute_ShouldReturnLoginResponseDTO_WhenCredentialsAreValid() {
        // Arrange
        String expectedToken = "jwt-token-123";
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            loginUserDTO.getLogin(), loginUserDTO.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(any(User.class))).thenReturn(expectedToken);
        when(loginMapper.toUserResponseDTO(anyString())).thenReturn(loginResponseDTO);

        // Act
        LoginResponseDTO result = loginUsecase.execute(loginUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(loginResponseDTO, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateJWT(user);
        verify(loginMapper).toUserResponseDTO(expectedToken);
    }

    @Test
    void execute_ShouldCallAuthenticationManagerWithCorrectToken_WhenExecuted() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(any(User.class))).thenReturn("token");
        when(loginMapper.toUserResponseDTO(anyString())).thenReturn(loginResponseDTO);

        // Act
        loginUsecase.execute(loginUserDTO);

        // Assert
        verify(authenticationManager).authenticate(argThat(token ->
            token.getPrincipal().equals(loginUserDTO.getLogin()) &&
            token.getCredentials().equals(loginUserDTO.getPassword())
        ));
    }

    @Test
    void execute_ShouldCallTokenServiceWithCorrectUser_WhenAuthenticated() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(user)).thenReturn("token");
        when(loginMapper.toUserResponseDTO(anyString())).thenReturn(loginResponseDTO);

        // Act
        loginUsecase.execute(loginUserDTO);

        // Assert
        verify(tokenService).generateJWT(user);
    }

    @Test
    void execute_ShouldCallMapperWithGeneratedToken_WhenTokenGenerated() {
        // Arrange
        String generatedToken = "generated-jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(any(User.class))).thenReturn(generatedToken);
        when(loginMapper.toUserResponseDTO(generatedToken)).thenReturn(loginResponseDTO);

        // Act
        loginUsecase.execute(loginUserDTO);

        // Assert
        verify(loginMapper).toUserResponseDTO(generatedToken);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        String token = "jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(any(User.class))).thenReturn(token);
        when(loginMapper.toUserResponseDTO(anyString())).thenReturn(loginResponseDTO);

        // Act
        loginUsecase.execute(loginUserDTO);

        // Assert
        var inOrder = inOrder(authenticationManager, tokenService, loginMapper);
        inOrder.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        inOrder.verify(tokenService).generateJWT(user);
        inOrder.verify(loginMapper).toUserResponseDTO(token);
    }

    @Test
    void execute_ShouldExtractUserFromAuthentication_WhenAuthenticationSuccessful() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(any(User.class))).thenReturn("token");
        when(loginMapper.toUserResponseDTO(anyString())).thenReturn(loginResponseDTO);

        // Act
        loginUsecase.execute(loginUserDTO);

        // Assert
        verify(authentication).getPrincipal();
        verify(tokenService).generateJWT(user);
    }

    @Test
    void execute_ShouldHandleDifferentCredentials_WhenCalled() {
        // Arrange
        LoginUserDTO differentCredentials = new LoginUserDTO();
        differentCredentials.setLogin("different@example.com");
        differentCredentials.setPassword("differentPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateJWT(any(User.class))).thenReturn("token");
        when(loginMapper.toUserResponseDTO(anyString())).thenReturn(loginResponseDTO);

        // Act
        loginUsecase.execute(differentCredentials);

        // Assert
        verify(authenticationManager).authenticate(argThat(token ->
            token.getPrincipal().equals("different@example.com") &&
            token.getCredentials().equals("differentPassword")
        ));
    }
}
