package com.chefia.infra.web;

import com.chefia.core.controllers.LoginController;
import com.chefia.users.model.LoginResponseDTO;
import com.chefia.users.model.LoginUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginApiControllerTest {

    @Mock
    private LoginController loginController;

    @InjectMocks
    private LoginApiController loginApiController;

    private LoginUserDTO loginUserDTO;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        loginUserDTO = new LoginUserDTO();
        loginUserDTO.setLogin("testuser");
        loginUserDTO.setPassword("password123");

        loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken("jwt-token-sample");
    }

    @Test
    void login_ShouldReturnLoginResponseDTO_WhenValidCredentials() {
        // Arrange
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        var result = loginApiController.login(loginUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(loginResponseDTO, result.getBody());
        verify(loginController).login(loginUserDTO);
    }

    @Test
    void login_ShouldCallLoginController_WhenExecuted() {
        // Arrange
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        loginApiController.login(loginUserDTO);

        // Assert
        verify(loginController, times(1)).login(loginUserDTO);
    }

    @Test
    void login_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        var result = loginApiController.login(loginUserDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void login_ShouldPassCorrectDTOToController_WhenExecuted() {
        // Arrange
        var specificLoginDTO = new LoginUserDTO();
        specificLoginDTO.setLogin("specificuser");
        specificLoginDTO.setPassword("specificpass");
        when(loginController.login(specificLoginDTO)).thenReturn(loginResponseDTO);

        // Act
        loginApiController.login(specificLoginDTO);

        // Assert
        verify(loginController).login(specificLoginDTO);
    }

    @Test
    void login_ShouldReturnResponseEntityWithCorrectBody_WhenExecuted() {
        // Arrange
        var specificResponse = new LoginResponseDTO();
        specificResponse.setToken("specific-jwt-token");
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(specificResponse);

        // Act
        var result = loginApiController.login(loginUserDTO);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(specificResponse, result.getBody());
        assertEquals("specific-jwt-token", result.getBody().getToken());
    }

    @Test
    void login_ShouldCreateResponseEntityCorrectly_WhenExecuted() {
        // Arrange
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        var result = loginApiController.login(loginUserDTO);

        // Assert
        assertNotNull(result);
        assertInstanceOf(ResponseEntity.class, result);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void login_ShouldHandleNullResponse_WhenControllerReturnsNull() {
        // Arrange
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(null);

        // Act
        var result = loginApiController.login(loginUserDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
        verify(loginController).login(loginUserDTO);
    }

    @Test
    void login_ShouldDelegateDirectlyToController_WhenExecuted() {
        // Arrange
        when(loginController.login(any(LoginUserDTO.class))).thenReturn(loginResponseDTO);

        // Act
        loginApiController.login(loginUserDTO);

        // Assert
        verify(loginController).login(loginUserDTO);
        verifyNoMoreInteractions(loginController);
    }
}
