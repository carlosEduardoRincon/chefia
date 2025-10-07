package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.UserGateway;
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
class UpdateUserStatusUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private UpdateUserStatusUsecaseImpl updateUserStatusUsecase;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setNrSeqUser(1L);
        existingUser.setName("Test User");
        existingUser.setEmail("test@example.com");
        existingUser.setLogin("testuser");
        existingUser.setPassword("hashedPassword");
        existingUser.setActive(true);
        existingUser.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));

        updateUserStatusUsecase = new UpdateUserStatusUsecaseImpl(userGateway);
    }

    @Test
    void execute_ShouldUpdateUserStatusToActive_WhenUserExistsAndStatusIsTrue() {
        // Arrange
        var userId = 1L;
        var newStatus = true;
        existingUser.setActive(false); // Initially inactive

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, newStatus);

        // Assert
        assertTrue(existingUser.isActive());
        assertNotNull(existingUser.getUpdatedAt());
        verify(userGateway).findById(userId);
        verify(userGateway).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldUpdateUserStatusToInactive_WhenUserExistsAndStatusIsFalse() {
        // Arrange
        var userId = 1L;
        var newStatus = false;
        existingUser.setActive(true); // Initially active

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, newStatus);

        // Assert
        assertFalse(existingUser.isActive());
        assertNotNull(existingUser.getUpdatedAt());
        verify(userGateway).findById(userId);
        verify(userGateway).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        var userId = 999L;
        var status = true;
        when(userGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(UserNotFoundException.class,
                () -> updateUserStatusUsecase.execute(userId, status));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userGateway).findById(userId);
        verify(userGateway, never()).updateUserStatus(anyLong(), any(User.class));
    }

    @Test
    void execute_ShouldCallUpdateUserStatusOnGateway_WhenUserExists() {
        // Arrange
        var userId = 1L;
        var status = false;
        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        verify(userGateway, times(1)).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var status = true;
        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        var inOrder = inOrder(userGateway);
        inOrder.verify(userGateway).findById(userId);
        inOrder.verify(userGateway).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldCallFindByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        var status = false;
        when(userGateway.findById(specificId)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(specificId, status);

        // Assert
        verify(userGateway).findById(specificId);
    }

    @Test
    void execute_ShouldUpdateTimestamp_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var status = true;
        var oldTimestamp = existingUser.getUpdatedAt();

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        assertNotNull(existingUser.getUpdatedAt());
        assertNotEquals(oldTimestamp, existingUser.getUpdatedAt());
        assertTrue(existingUser.getUpdatedAt().isAfter(oldTimestamp));
        verify(userGateway).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldSetCorrectStatus_WhenStatusIsTrue() {
        // Arrange
        var userId = 1L;
        var status = true;
        existingUser.setActive(false);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        assertTrue(existingUser.isActive());
        verify(userGateway).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldSetCorrectStatus_WhenStatusIsFalse() {
        // Arrange
        var userId = 1L;
        var status = false;
        existingUser.setActive(true);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        assertFalse(existingUser.isActive());
        verify(userGateway).updateUserStatus(userId, existingUser);
    }

    @Test
    void execute_ShouldPassCorrectUserToGateway_WhenUpdatingStatus() {
        // Arrange
        var userId = 3L;
        var status = true;
        var specificUser = new User();
        specificUser.setNrSeqUser(userId);
        specificUser.setName("Specific User");
        specificUser.setEmail("specific@example.com");
        specificUser.setActive(false);

        when(userGateway.findById(userId)).thenReturn(Optional.of(specificUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        verify(userGateway).updateUserStatus(userId, specificUser);
        assertTrue(specificUser.isActive());
    }

    @Test
    void execute_ShouldNotModifyOtherUserFields_WhenUpdatingStatus() {
        // Arrange
        var userId = 1L;
        var status = false;
        var originalName = existingUser.getName();
        var originalEmail = existingUser.getEmail();
        var originalLogin = existingUser.getLogin();
        var originalPassword = existingUser.getPassword();

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        assertEquals(originalName, existingUser.getName());
        assertEquals(originalEmail, existingUser.getEmail());
        assertEquals(originalLogin, existingUser.getLogin());
        assertEquals(originalPassword, existingUser.getPassword());
        assertFalse(existingUser.isActive());
    }

    @Test
    void execute_ShouldHandleDifferentUserIds_WhenCalled() {
        // Arrange
        var userId1 = 1L;
        var userId2 = 2L;
        var status = true;

        when(userGateway.findById(userId1)).thenReturn(Optional.of(existingUser));
        when(userGateway.findById(userId2)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId1, status);
        updateUserStatusUsecase.execute(userId2, status);

        // Assert
        verify(userGateway).findById(userId1);
        verify(userGateway).findById(userId2);
        verify(userGateway).updateUserStatus(userId1, existingUser);
        verify(userGateway).updateUserStatus(userId2, existingUser);
    }

    @Test
    void execute_ShouldDelegateDirectlyToGateway_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var status = true;
        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        updateUserStatusUsecase.execute(userId, status);

        // Assert
        verify(userGateway).findById(userId);
        verify(userGateway).updateUserStatus(userId, existingUser);
        verifyNoMoreInteractions(userGateway);
    }
}
