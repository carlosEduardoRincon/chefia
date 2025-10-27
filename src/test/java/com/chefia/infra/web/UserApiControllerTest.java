package com.chefia.infra.web;

import com.chefia.core.controllers.UserController;
import com.chefia.users.model.*;
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
class UserApiControllerTest {

    @Mock
    private UserController userController;

    @InjectMocks
    private UserApiController userApiController;

    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;
    private UserDTO userDTO;
    private PaginatedUsersDTO paginatedUsersDTO;
    private ChangePasswordDTO changePasswordDTO;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Test User");
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setLogin("testuser");

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName("Updated User");

        userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");

        paginatedUsersDTO = new PaginatedUsersDTO();
        paginatedUsersDTO.setPage(0);
        paginatedUsersDTO.setPerPage(10);
        paginatedUsersDTO.setTotal(1L);

        changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldpass");
        changePasswordDTO.setNewPassword("newpass");
    }

    @Test
    void createUser_ShouldReturnCreatedUserDTO_WhenValidData() {
        // Arrange
        when(userController.saveUser(any(CreateUserDTO.class))).thenReturn(userDTO);

        // Act
        var result = userApiController.createUser(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(userDTO, result.getBody());
        verify(userController).saveUser(createUserDTO);
    }

    @Test
    void createUser_ShouldReturn201Status_WhenExecuted() {
        // Arrange
        when(userController.saveUser(any(CreateUserDTO.class))).thenReturn(userDTO);

        // Act
        var result = userApiController.createUser(createUserDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void getUser_ShouldReturnUserDTO_WhenUserExists() {
        // Arrange
        var userId = 1L;
        when(userController.findById(anyLong())).thenReturn(userDTO);

        // Act
        var result = userApiController.getUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDTO, result.getBody());
        verify(userController).findById(userId);
    }

    @Test
    void getUser_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(userController.findById(specificId)).thenReturn(userDTO);

        // Act
        userApiController.getUser(specificId);

        // Assert
        verify(userController).findById(specificId);
    }

    @Test
    void listUsers_ShouldReturnPaginatedUsersDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userController.findAll(anyInt(), anyInt())).thenReturn(paginatedUsersDTO);

        // Act
        var result = userApiController.listUsers(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(paginatedUsersDTO, result.getBody());
        verify(userController).findAll(page, perPage);
    }

    @Test
    void listUsers_ShouldPassCorrectPaginationParameters_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 25;
        when(userController.findAll(page, perPage)).thenReturn(paginatedUsersDTO);

        // Act
        userApiController.listUsers(page, perPage);

        // Assert
        verify(userController).findAll(page, perPage);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenValidData() {
        // Arrange
        var userId = 1L;
        when(userController.updateUser(anyLong(), any(UpdateUserDTO.class))).thenReturn(userDTO);

        // Act
        var result = userApiController.updateUser(userId, updateUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDTO, result.getBody());
        verify(userController).updateUser(userId, updateUserDTO);
    }

    @Test
    void updateUser_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var userId = 3L;
        var specificUpdateDTO = new UpdateUserDTO();
        specificUpdateDTO.setName("Specific Name");
        when(userController.updateUser(userId, specificUpdateDTO)).thenReturn(userDTO);

        // Act
        userApiController.updateUser(userId, specificUpdateDTO);

        // Assert
        verify(userController).updateUser(userId, specificUpdateDTO);
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenExecuted() {
        // Arrange
        var userId = 1L;
        doNothing().when(userController).deleteUser(anyLong());

        // Act
        var result = userApiController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(userController).deleteUser(userId);
    }

    @Test
    void deleteUser_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        doNothing().when(userController).deleteUser(specificId);

        // Act
        userApiController.deleteUser(specificId);

        // Assert
        verify(userController).deleteUser(specificId);
    }

    @Test
    void enableUser_ShouldReturnNoContent_WhenExecuted() {
        // Arrange
        var userId = 1L;
        doNothing().when(userController).changeUserStatus(anyLong(), anyBoolean());

        // Act
        var result = userApiController.enableUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(userController).changeUserStatus(userId, Boolean.TRUE);
    }

    @Test
    void enableUser_ShouldCallControllerWithTrueStatus_WhenExecuted() {
        // Arrange
        var userId = 2L;
        doNothing().when(userController).changeUserStatus(userId, Boolean.TRUE);

        // Act
        userApiController.enableUser(userId);

        // Assert
        verify(userController).changeUserStatus(userId, Boolean.TRUE);
    }

    @Test
    void disableUser_ShouldReturnNoContent_WhenExecuted() {
        // Arrange
        var userId = 1L;
        doNothing().when(userController).changeUserStatus(anyLong(), anyBoolean());

        // Act
        var result = userApiController.disableUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(userController).changeUserStatus(userId, Boolean.FALSE);
    }

    @Test
    void disableUser_ShouldCallControllerWithFalseStatus_WhenExecuted() {
        // Arrange
        var userId = 3L;
        doNothing().when(userController).changeUserStatus(userId, Boolean.FALSE);

        // Act
        userApiController.disableUser(userId);

        // Assert
        verify(userController).changeUserStatus(userId, Boolean.FALSE);
    }

    @Test
    void changePassword_ShouldReturnNoContent_WhenExecuted() {
        // Arrange
        var userId = 1L;
        doNothing().when(userController).changePassword(anyLong(), any(ChangePasswordDTO.class));

        // Act
        var result = userApiController.changePassword(userId, changePasswordDTO);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(userController).changePassword(userId, changePasswordDTO);
    }

    @Test
    void changePassword_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var userId = 4L;
        var specificPasswordDTO = new ChangePasswordDTO();
        specificPasswordDTO.setOldPassword("specific-old");
        specificPasswordDTO.setNewPassword("specific-new");
        doNothing().when(userController).changePassword(userId, specificPasswordDTO);

        // Act
        userApiController.changePassword(userId, specificPasswordDTO);

        // Assert
        verify(userController).changePassword(userId, specificPasswordDTO);
    }

    @Test
    void createUser_ShouldDelegateDirectlyToController_WhenExecuted() {
        // Arrange
        when(userController.saveUser(any(CreateUserDTO.class))).thenReturn(userDTO);

        // Act
        userApiController.createUser(createUserDTO);

        // Assert
        verify(userController).saveUser(createUserDTO);
        verifyNoMoreInteractions(userController);
    }
}
