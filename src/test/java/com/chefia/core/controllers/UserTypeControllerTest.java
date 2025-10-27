package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.usertype.*;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
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
class UserTypeControllerTest {

    @Mock
    private CreateUserTypeUsecase createUserTypeUsecase;

    @Mock
    private ReadUserTypeUsecase readUserTypeUsecase;

    @Mock
    private ReadAllUserTypeUsecase readAllUserTypeUsecase;

    @Mock
    private UpdateUserTypeUsecase updateUserTypeUsecase;

    @Mock
    private DeleteUserTypeUsecase deleteUserTypeUsecase;

    @InjectMocks
    private UserTypeController userTypeController;

    private CreateUserTypeDTO createUserTypeDTO;
    private UpdateUserTypeDTO updateUserTypeDTO;
    private UserTypeDTO userTypeDTO;
    private PaginatedUserTypeDTO paginatedUserTypeDTO;

    @BeforeEach
    void setUp() {
        createUserTypeDTO = new CreateUserTypeDTO();
        updateUserTypeDTO = new UpdateUserTypeDTO();
        userTypeDTO = new UserTypeDTO();
        paginatedUserTypeDTO = new PaginatedUserTypeDTO();
    }

    @Test
    void saveUserType_ShouldReturnUserTypeDTO_WhenValidData() {
        // Arrange
        when(createUserTypeUsecase.execute(any(CreateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        var result = userTypeController.saveUserType(createUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(createUserTypeUsecase, times(1)).execute(createUserTypeDTO);
    }

    @Test
    void findById_ShouldReturnUserTypeDTO_WhenValidId() {
        // Arrange
        var userTypeId = 1L;
        when(readUserTypeUsecase.execute(anyLong())).thenReturn(userTypeDTO);

        // Act
        var result = userTypeController.findById(userTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(readUserTypeUsecase, times(1)).execute(userTypeId);
    }

    @Test
    void findAll_ShouldReturnPaginatedUserTypeDTO_WhenValidParameters() {
        // Arrange
        var page = 1;
        var perPage = 10;
        when(readAllUserTypeUsecase.execute(anyInt(), anyInt())).thenReturn(paginatedUserTypeDTO);

        // Act
        var result = userTypeController.findAll(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(paginatedUserTypeDTO, result);
        verify(readAllUserTypeUsecase, times(1)).execute(page, perPage);
    }

    @Test
    void updateUserType_ShouldReturnUserTypeDTO_WhenValidData() {
        // Arrange
        var userTypeId = 1L;
        when(updateUserTypeUsecase.execute(anyLong(), any(UpdateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        var result = userTypeController.updateUserType(userTypeId, updateUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(updateUserTypeUsecase, times(1)).execute(userTypeId, updateUserTypeDTO);
    }

    @Test
    void deleteUserType_ShouldCallUsecase_WhenValidId() {
        // Arrange
        var userTypeId = 1L;

        // Act
        userTypeController.deleteUserType(userTypeId);

        // Assert
        verify(deleteUserTypeUsecase, times(1)).execute(userTypeId);
    }
}
