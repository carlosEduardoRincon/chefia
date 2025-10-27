package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.gateway.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTypeUsecaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @InjectMocks
    private DeleteUserTypeUsecaseImpl deleteUserTypeUsecase;
    
    @BeforeEach
    void setUp() {
        deleteUserTypeUsecase = new DeleteUserTypeUsecaseImpl(userTypeGateway);
    }

    @Test
    void execute_ShouldCallDeleteByIdOnGateway_WhenExecuted() {
        // Arrange
        var menuItemId = 1L;

        // Act
        deleteUserTypeUsecase.execute(menuItemId);

        // Assert
        verify(userTypeGateway).deleteById(menuItemId);
    }

    @Test
    void execute_ShouldCallDeleteByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 5L;

        // Act
        deleteUserTypeUsecase.execute(specificId);

        // Assert
        verify(userTypeGateway).deleteById(specificId);
    }

    @Test
    void execute_ShouldCallDeleteByIdOnce_WhenExecuted() {
        // Arrange
        var menuItemId = 1L;

        // Act
        deleteUserTypeUsecase.execute(menuItemId);

        // Assert
        verify(userTypeGateway, times(1)).deleteById(menuItemId);
    }

    @Test
    void execute_ShouldHandleNullId_WhenCalled() {
        // Arrange
        Long nullId = null;

        // Act
        deleteUserTypeUsecase.execute(nullId);

        // Assert
        verify(userTypeGateway).deleteById(nullId);
    }

    @Test
    void execute_ShouldHandleZeroId_WhenCalled() {
        // Arrange
        var zeroId = 0L;

        // Act
        deleteUserTypeUsecase.execute(zeroId);

        // Assert
        verify(userTypeGateway).deleteById(zeroId);
    }

    @Test
    void execute_ShouldHandleNegativeId_WhenCalled() {
        // Arrange
        var negativeId = -1L;

        // Act
        deleteUserTypeUsecase.execute(negativeId);

        // Assert
        verify(userTypeGateway).deleteById(negativeId);
    }

    @Test
    void execute_ShouldHandleLargeId_WhenCalled() {
        // Arrange
        var largeId = Long.MAX_VALUE;

        // Act
        deleteUserTypeUsecase.execute(largeId);

        // Assert
        verify(userTypeGateway).deleteById(largeId);
    }

    @Test
    void execute_ShouldDelegateDirectlyToGateway_WhenExecuted() {
        // Arrange
        var menuItemId = 123L;

        // Act
        deleteUserTypeUsecase.execute(menuItemId);

        // Assert
        verify(userTypeGateway).deleteById(menuItemId);
        verifyNoMoreInteractions(userTypeGateway);
    }

    @Test
    void execute_ShouldNotPerformValidation_WhenExecuted() {
        // Arrange
        var menuItemId = 999L;

        // Act
        deleteUserTypeUsecase.execute(menuItemId);

        // Assert
        verify(userTypeGateway).deleteById(menuItemId);
        verify(userTypeGateway, never()).findById(anyLong());
        verifyNoMoreInteractions(userTypeGateway);
    }

    @Test
    void execute_ShouldAcceptMultipleSequentialCalls_WhenExecuted() {
        // Arrange
        var firstId = 1L;
        var secondId = 2L;
        var thirdId = 3L;

        // Act
        deleteUserTypeUsecase.execute(firstId);
        deleteUserTypeUsecase.execute(secondId);
        deleteUserTypeUsecase.execute(thirdId);

        // Assert
        verify(userTypeGateway).deleteById(firstId);
        verify(userTypeGateway).deleteById(secondId);
        verify(userTypeGateway).deleteById(thirdId);
        verify(userTypeGateway, times(3)).deleteById(anyLong());
    }
}
