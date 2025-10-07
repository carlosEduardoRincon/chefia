package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UpdateUserUsecaseImpl updateUserUsecase;

    private User existingUser;
    private UpdateUserDTO updateUserDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setNrSeqUser(1L);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setLogin("oldlogin");
        existingUser.setPassword("oldHashedPassword");
        existingUser.setActive(true);
        existingUser.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));

        updateUserDTO = new UpdateUserDTO();
        userDTO = new UserDTO();

        updateUserUsecase = new UpdateUserUsecaseImpl(userGateway, userMapper);
    }

    @Test
    void execute_ShouldReturnUpdatedUserDTO_WhenUserExists() {
        // Arrange
        var userId = 1L;
        var newName = "Updated Name";
        var newEmail = "updated@example.com";
        var newLogin = "updatedlogin";

        updateUserDTO.setName(newName);
        updateUserDTO.setEmail(newEmail);
        updateUserDTO.setLogin(newLogin);

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        var result = updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userGateway).findById(userId);
        verify(userGateway).update(userId, existingUser);
        verify(userMapper).toUserResponseDTO(existingUser);

        // Verify that the entity was updated with new values
        assertEquals(newName, existingUser.getName());
        assertEquals(newEmail, existingUser.getEmail());
        assertEquals(newLogin, existingUser.getLogin());
        assertNotNull(existingUser.getUpdatedAt());
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        var userId = 999L;
        when(userGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(UserNotFoundException.class,
                    () -> updateUserUsecase.execute(userId, updateUserDTO));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userGateway).findById(userId);
        verify(userGateway, never()).update(anyLong(), any(User.class));
        verify(userMapper, never()).toUserResponseDTO(any(User.class));
    }

    @Test
    void execute_ShouldCallUpdateOnGateway_WhenUserExists() {
        // Arrange
        var userId = 1L;
        updateUserDTO.setName("New Name");
        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        verify(userGateway, times(1)).update(userId, existingUser);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var userId = 1L;
        updateUserDTO.setName("Test Name");
        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        var inOrder = inOrder(userGateway, userMapper);
        inOrder.verify(userGateway).findById(userId);
        inOrder.verify(userGateway).update(userId, existingUser);
        inOrder.verify(userMapper).toUserResponseDTO(existingUser);
    }

    @Test
    void execute_ShouldUpdateEntityFieldsDirectly_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var expectedName = "Direct Update Name";
        var expectedEmail = "directupdate@example.com";
        var expectedLogin = "directupdatelogin";

        updateUserDTO.setName(expectedName);
        updateUserDTO.setEmail(expectedEmail);
        updateUserDTO.setLogin(expectedLogin);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertEquals(expectedName, existingUser.getName());
        assertEquals(expectedEmail, existingUser.getEmail());
        assertEquals(expectedLogin, existingUser.getLogin());
        assertNotNull(existingUser.getUpdatedAt());
    }

    @Test
    void execute_ShouldCallFindByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        when(userGateway.findById(specificId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(specificId, updateUserDTO);

        // Assert
        verify(userGateway).findById(specificId);
    }

    @Test
    void execute_ShouldUpdateNameCorrectly_WhenNameIsChanged() {
        // Arrange
        var userId = 1L;
        var newName = "New User Name";
        updateUserDTO.setName(newName);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(existingUser)).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertEquals(newName, existingUser.getName());
        verify(userGateway).update(userId, existingUser);
        verify(userMapper).toUserResponseDTO(existingUser);
    }

    @Test
    void execute_ShouldUpdateEmailCorrectly_WhenEmailIsChanged() {
        // Arrange
        var userId = 1L;
        var newEmail = "newemail@example.com";
        updateUserDTO.setEmail(newEmail);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(existingUser)).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertEquals(newEmail, existingUser.getEmail());
        verify(userGateway).update(userId, existingUser);
        verify(userMapper).toUserResponseDTO(existingUser);
    }

    @Test
    void execute_ShouldUpdateLoginCorrectly_WhenLoginIsChanged() {
        // Arrange
        var userId = 1L;
        var newLogin = "newlogin";
        updateUserDTO.setLogin(newLogin);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(existingUser)).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertEquals(newLogin, existingUser.getLogin());
        verify(userGateway).update(userId, existingUser);
        verify(userMapper).toUserResponseDTO(existingUser);
    }

    @Test
    void execute_ShouldUpdateTimestamp_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var oldTimestamp = existingUser.getUpdatedAt();
        updateUserDTO.setName("Updated Name");

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(existingUser)).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertNotNull(existingUser.getUpdatedAt());
        assertNotEquals(oldTimestamp, existingUser.getUpdatedAt());
        assertTrue(existingUser.getUpdatedAt().isAfter(oldTimestamp));
        verify(userGateway).update(userId, existingUser);
    }

    @Test
    void execute_ShouldHandleNullValues_WhenDTOHasNullFields() {
        // Arrange
        var userId = 1L;
        updateUserDTO.setName(null);
        updateUserDTO.setEmail(null);
        updateUserDTO.setLogin(null);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        assertNull(existingUser.getName());
        assertNull(existingUser.getEmail());
        assertNull(existingUser.getLogin());
        assertNotNull(existingUser.getUpdatedAt());
        verify(userGateway).update(userId, existingUser);
    }

    @Test
    void execute_ShouldPassCorrectUserToMapper_WhenMappingResponse() {
        // Arrange
        var userId = 1L;
        var specificUser = new User();
        specificUser.setNrSeqUser(userId);
        specificUser.setName("Specific User");
        specificUser.setEmail("specific@example.com");

        updateUserDTO.setName("Updated Name");

        when(userGateway.findById(userId)).thenReturn(Optional.of(specificUser));
        when(userMapper.toUserResponseDTO(specificUser)).thenReturn(userDTO);

        // Act
        updateUserUsecase.execute(userId, updateUserDTO);

        // Assert
        verify(userMapper).toUserResponseDTO(specificUser);
        assertEquals("Updated Name", specificUser.getName());
    }
}
