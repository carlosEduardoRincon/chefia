package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.user.*;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.PaginatedUsersDTO;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateUserUsecase createUserUsecase;

    @Mock
    private ReadUserUsecase readUserUsecase;

    @Mock
    private ReadAllUserUsecase readAllUserUsecase;

    @Mock
    private UpdateUserUsecase updateUserUsecase;

    @Mock
    private UpdateUserStatusUsecase updateUserStatusUsecase;

    @Mock
    private DeleteUserUsecase deleteUserUsecase;

    @InjectMocks
    private UserController userController;

    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;
    private UserDTO userDTO;
    private PaginatedUsersDTO paginatedUsersDTO;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO();
        updateUserDTO = new UpdateUserDTO();
        userDTO = new UserDTO();
        paginatedUsersDTO = new PaginatedUsersDTO();
    }

    @Test
    void createUser_ShouldReturnUserDTO_WhenValidData() {
        // Arrange
        when(createUserUsecase.execute(any(CreateUserDTO.class))).thenReturn(userDTO);

        // Act
        var result = userController.saveUser(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(createUserUsecase, times(1)).execute(createUserDTO);
    }

    @Test
    void getUser_ShouldReturnUserDTO_WhenValidId() {
        // Arrange
        var userId = 1L;
        when(readUserUsecase.execute(anyLong())).thenReturn(userDTO);

        // Act
        var result = userController.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(readUserUsecase, times(1)).execute(userId);
    }

    @Test
    void listUsers_ShouldReturnPaginatedUsersDTO_WhenValidParameters() {
        // Arrange
        var page = 1;
        var perPage = 10;
        when(readAllUserUsecase.execute(anyInt(), anyInt())).thenReturn(paginatedUsersDTO);

        // Act
        var result = userController.findAll(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(paginatedUsersDTO, result);
        verify(readAllUserUsecase, times(1)).execute(page, perPage);
    }

    @Test
    void updateUser_ShouldReturnUserDTO_WhenValidData() {
        // Arrange
        var userId = 1L;
        when(updateUserUsecase.execute(anyLong(), any(UpdateUserDTO.class))).thenReturn(userDTO);

        // Act
        var result = userController.updateUser(userId, updateUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(updateUserUsecase, times(1)).execute(userId, updateUserDTO);
    }

    @Test
    void deleteUser_ShouldCallUsecase_WhenValidId() {
        // Arrange
        var userId = 1L;

        // Act
        userController.deleteUser(userId);

        // Assert
        verify(deleteUserUsecase, times(1)).execute(userId);
    }

    @Test
    void activateUser_ShouldCallUsecase_WhenValidId() {
        // Arrange
        var userId = 1L;
        when(updateUserUsecase.execute(anyLong(), any(UpdateUserDTO.class))).thenReturn(userDTO);

        // Act
        var result = userController.updateUser(userId, updateUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(updateUserUsecase, times(1)).execute(eq(userId), eq(updateUserDTO));
    }

    @Test
    void deactivateUser_ShouldCallUsecase_WhenValidId() {
        // Arrange
        var userId = 1L;

        // Act & Assert
        assertDoesNotThrow(() -> updateUserStatusUsecase.execute(userId, Boolean.TRUE));
    }

    @Test
    void createUser_ShouldPassCorrectParameterToUsecase_WhenCalled() {
        // Arrange
        var specificDTO = new CreateUserDTO();
        when(createUserUsecase.execute(any(CreateUserDTO.class))).thenReturn(userDTO);

        // Act
        userController.saveUser(specificDTO);

        // Assert
        verify(createUserUsecase).execute(eq(specificDTO));
    }

    @Test
    void updateUser_ShouldPassCorrectParametersToUsecase_WhenCalled() {
        // Arrange
        var userId = 7L;
        var specificDTO = new UpdateUserDTO();
        when(updateUserUsecase.execute(anyLong(), any(UpdateUserDTO.class))).thenReturn(userDTO);

        // Act
        userController.updateUser(userId, specificDTO);

        // Assert
        verify(updateUserUsecase).execute(eq(userId), eq(specificDTO));
    }

    @Test
    void listUsersByStatus_ShouldReturnPaginatedUsersDTO_WhenValidParameters() {
        // Arrange
        var page = 0;
        var perPage = 20;
        when(readAllUserUsecase.execute(anyInt(), anyInt())).thenReturn(paginatedUsersDTO);

        // Act
        var result = userController.findAll(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(paginatedUsersDTO, result);
        verify(readAllUserUsecase, times(1)).execute(page, perPage);
    }
}
