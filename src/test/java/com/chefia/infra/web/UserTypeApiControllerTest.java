package com.chefia.infra.web;

import com.chefia.core.controllers.UserTypeController;
import com.chefia.usertypes.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeApiControllerTest {

    @Mock
    private UserTypeController userTypeController;

    @InjectMocks
    private UserTypeApiController userTypeApiController;

    private CreateUserTypeDTO createUserTypeDTO;
    private UpdateUserTypeDTO updateUserTypeDTO;
    private UserTypeDTO userTypeDTO;
    private PaginatedUserTypeDTO paginatedUserTypeDTO;

    @BeforeEach
    void setUp() {
        createUserTypeDTO = new CreateUserTypeDTO();
        createUserTypeDTO.setName("Administrator");
        createUserTypeDTO.setDescription("System administrator");

        updateUserTypeDTO = new UpdateUserTypeDTO();
        updateUserTypeDTO.setName("Updated Admin");

        userTypeDTO = new UserTypeDTO();
        userTypeDTO.setName("Administrator");
        userTypeDTO.setDescription("System administrator");

        paginatedUserTypeDTO = new PaginatedUserTypeDTO();
        paginatedUserTypeDTO.setPage(0);
        paginatedUserTypeDTO.setPerPage(10);
        paginatedUserTypeDTO.setTotal(1L);
    }

    @Test
    void createUserType_ShouldReturnCreatedUserTypeDTO_WhenValidData() {
        // Arrange
        when(userTypeController.saveUserType(any(CreateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        var result = userTypeApiController.createUserType(createUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(userTypeDTO, result.getBody());
        verify(userTypeController).saveUserType(createUserTypeDTO);
    }

    @Test
    void createUserType_ShouldReturn201Status_WhenExecuted() {
        // Arrange
        when(userTypeController.saveUserType(any(CreateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        var result = userTypeApiController.createUserType(createUserTypeDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void createUserType_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        when(userTypeController.saveUserType(any(CreateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        userTypeApiController.createUserType(createUserTypeDTO);

        // Assert
        verify(userTypeController, times(1)).saveUserType(createUserTypeDTO);
    }

    @Test
    void getUserType_ShouldReturnUserTypeDTO_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeController.findById(anyLong())).thenReturn(userTypeDTO);

        // Act
        var result = userTypeApiController.getUserType(userTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userTypeDTO, result.getBody());
        verify(userTypeController).findById(userTypeId);
    }

    @Test
    void getUserType_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(userTypeController.findById(specificId)).thenReturn(userTypeDTO);

        // Act
        userTypeApiController.getUserType(specificId);

        // Assert
        verify(userTypeController).findById(specificId);
    }

    @Test
    void listUserTypes_ShouldReturnPaginatedUserTypeDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userTypeController.findAll(anyInt(), anyInt())).thenReturn(paginatedUserTypeDTO);

        // Act
        var result = userTypeApiController.listUserTypes(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(paginatedUserTypeDTO, result.getBody());
        verify(userTypeController).findAll(page, perPage);
    }

    @Test
    void listUserTypes_ShouldPassCorrectPaginationParameters_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 25;
        when(userTypeController.findAll(page, perPage)).thenReturn(paginatedUserTypeDTO);

        // Act
        userTypeApiController.listUserTypes(page, perPage);

        // Assert
        verify(userTypeController).findAll(page, perPage);
    }

    @Test
    void listUserTypes_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        when(userTypeController.findAll(anyInt(), anyInt())).thenReturn(paginatedUserTypeDTO);

        // Act
        var result = userTypeApiController.listUserTypes(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateUserType_ShouldReturnUpdatedUserTypeDTO_WhenValidData() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeController.updateUserType(anyLong(), any(UpdateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        var result = userTypeApiController.updateUserType(userTypeId, updateUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userTypeDTO, result.getBody());
        verify(userTypeController).updateUserType(userTypeId, updateUserTypeDTO);
    }

    @Test
    void updateUserType_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var userTypeId = 3L;
        var specificUpdateDTO = new UpdateUserTypeDTO();
        specificUpdateDTO.setName("Specific Name");
        when(userTypeController.updateUserType(userTypeId, specificUpdateDTO)).thenReturn(userTypeDTO);

        // Act
        userTypeApiController.updateUserType(userTypeId, specificUpdateDTO);

        // Assert
        verify(userTypeController).updateUserType(userTypeId, specificUpdateDTO);
    }

    @Test
    void updateUserType_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeController.updateUserType(anyLong(), any(UpdateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        var result = userTypeApiController.updateUserType(userTypeId, updateUserTypeDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteUserType_ShouldReturnNoContent_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        doNothing().when(userTypeController).deleteUserType(anyLong());

        // Act
        var result = userTypeApiController.deleteUserType(userTypeId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(userTypeController).deleteUserType(userTypeId);
    }

    @Test
    void deleteUserType_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        doNothing().when(userTypeController).deleteUserType(specificId);

        // Act
        userTypeApiController.deleteUserType(specificId);

        // Assert
        verify(userTypeController).deleteUserType(specificId);
    }

    @Test
    void deleteUserType_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        doNothing().when(userTypeController).deleteUserType(userTypeId);

        // Act
        userTypeApiController.deleteUserType(userTypeId);

        // Assert
        verify(userTypeController, times(1)).deleteUserType(userTypeId);
    }

    @Test
    void createUserType_ShouldPassCorrectDTOToController_WhenExecuted() {
        // Arrange
        var specificCreateDTO = new CreateUserTypeDTO();
        specificCreateDTO.setName("Manager");
        specificCreateDTO.setDescription("Restaurant Manager");
        when(userTypeController.saveUserType(specificCreateDTO)).thenReturn(userTypeDTO);

        // Act
        userTypeApiController.createUserType(specificCreateDTO);

        // Assert
        verify(userTypeController).saveUserType(specificCreateDTO);
    }

    @Test
    void createUserType_ShouldDelegateDirectlyToController_WhenExecuted() {
        // Arrange
        when(userTypeController.saveUserType(any(CreateUserTypeDTO.class))).thenReturn(userTypeDTO);

        // Act
        userTypeApiController.createUserType(createUserTypeDTO);

        // Assert
        verify(userTypeController).saveUserType(createUserTypeDTO);
        verifyNoMoreInteractions(userTypeController);
    }

    @Test
    void getUserType_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeController.findById(userTypeId)).thenReturn(userTypeDTO);

        // Act
        var result = userTypeApiController.getUserType(userTypeId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateUserType_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeController.updateUserType(userTypeId, updateUserTypeDTO)).thenReturn(userTypeDTO);

        // Act
        userTypeApiController.updateUserType(userTypeId, updateUserTypeDTO);

        // Assert
        verify(userTypeController, times(1)).updateUserType(userTypeId, updateUserTypeDTO);
    }
}
