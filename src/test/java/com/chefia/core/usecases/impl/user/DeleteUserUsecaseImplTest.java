package com.chefia.core.usecases.impl.user;

import com.chefia.core.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private DeleteUserUsecaseImpl deleteUserUsecase;

    @Test
    void execute_ShouldCallDeleteOnGateway_WhenExecuted() {
        // Arrange
        var userId = 1L;

        // Act
        deleteUserUsecase.execute(userId);

        // Assert
        verify(userGateway).deleteById(userId);
    }

    @Test
    void execute_ShouldCallDeleteWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 5L;

        // Act
        deleteUserUsecase.execute(specificId);

        // Assert
        verify(userGateway).deleteById(specificId);
    }

    @Test
    void execute_ShouldCallDeleteOnce_WhenExecuted() {
        // Arrange
        var userId = 1L;

        // Act
        deleteUserUsecase.execute(userId);

        // Assert
        verify(userGateway, times(1)).deleteById(userId);
    }

    @Test
    void execute_ShouldNotCallAnyOtherMethods_WhenExecuted() {
        // Arrange
        var userId = 1L;

        // Act
        deleteUserUsecase.execute(userId);

        // Assert
        verify(userGateway).deleteById(userId);
        verifyNoMoreInteractions(userGateway);
    }

    @Test
    void execute_ShouldHandleDifferentUserIds_WhenExecuted() {
        // Arrange
        var userId1 = 10L;
        var userId2 = 20L;

        // Act
        deleteUserUsecase.execute(userId1);
        deleteUserUsecase.execute(userId2);

        // Assert
        verify(userGateway).deleteById(userId1);
        verify(userGateway).deleteById(userId2);
    }

    @Test
    void execute_ShouldCallDeleteByIdDirectly_WhenExecuted() {
        // Arrange
        var userId = 42L;

        // Act
        deleteUserUsecase.execute(userId);

        // Assert
        verify(userGateway, only()).deleteById(userId);
    }

    @Test
    void execute_ShouldWorkWithZeroId_WhenExecuted() {
        // Arrange
        var userId = 0L;

        // Act
        deleteUserUsecase.execute(userId);

        // Assert
        verify(userGateway).deleteById(userId);
    }

    @Test
    void execute_ShouldWorkWithLargeId_WhenExecuted() {
        // Arrange
        var userId = 999999L;

        // Act
        deleteUserUsecase.execute(userId);

        // Assert
        verify(userGateway).deleteById(userId);
    }
}
