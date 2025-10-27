package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.login.LoginUsecase;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginUsecase loginUsecase;

    @InjectMocks
    private LoginController loginController;

    private LoginUserDTO loginUserDTO;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        loginUserDTO = new LoginUserDTO();
        loginResponseDTO = new LoginResponseDTO();
    }

    @Test
    void login_ShouldReturnLoginResponseDTO_WhenValidCredentials() {
        // Arrange
        when(loginUsecase.execute(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        var result = loginController.login(loginUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(loginResponseDTO, result);
        verify(loginUsecase, times(1)).execute(loginUserDTO);
    }

    @Test
    void login_ShouldCallUsecaseOnce_WhenCalled() {
        // Arrange
        when(loginUsecase.execute(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        loginController.login(loginUserDTO);

        // Assert
        verify(loginUsecase, times(1)).execute(loginUserDTO);
    }

    @Test
    void login_ShouldPassCorrectParameter_WhenCalled() {
        // Arrange
        when(loginUsecase.execute(loginUserDTO)).thenReturn(loginResponseDTO);

        // Act
        loginController.login(loginUserDTO);

        // Assert
        verify(loginUsecase).execute(loginUserDTO);
    }
}
