package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.gateway.MenuItemGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUsecaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @InjectMocks
    private DeleteMenuItemUsecaseImpl deleteMenuItemUsecase;

    @BeforeEach
    void setUp() {
        deleteMenuItemUsecase = new DeleteMenuItemUsecaseImpl(menuItemGateway);
    }

    @Test
    void execute_ShouldCallDeleteByIdOnGateway_WhenExecuted() {
        // Arrange
        var menuItemId = 1L;

        // Act
        deleteMenuItemUsecase.execute(menuItemId);

        // Assert
        verify(menuItemGateway).deleteById(menuItemId);
    }

    @Test
    void execute_ShouldCallDeleteByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 5L;

        // Act
        deleteMenuItemUsecase.execute(specificId);

        // Assert
        verify(menuItemGateway).deleteById(specificId);
    }

    @Test
    void execute_ShouldCallDeleteByIdOnce_WhenExecuted() {
        // Arrange
        var menuItemId = 1L;

        // Act
        deleteMenuItemUsecase.execute(menuItemId);

        // Assert
        verify(menuItemGateway, times(1)).deleteById(menuItemId);
    }

    @Test
    void execute_ShouldHandleNullId_WhenCalled() {
        // Arrange
        Long nullId = null;

        // Act
        deleteMenuItemUsecase.execute(nullId);

        // Assert
        verify(menuItemGateway).deleteById(nullId);
    }

    @Test
    void execute_ShouldHandleZeroId_WhenCalled() {
        // Arrange
        var zeroId = 0L;

        // Act
        deleteMenuItemUsecase.execute(zeroId);

        // Assert
        verify(menuItemGateway).deleteById(zeroId);
    }

    @Test
    void execute_ShouldHandleNegativeId_WhenCalled() {
        // Arrange
        var negativeId = -1L;

        // Act
        deleteMenuItemUsecase.execute(negativeId);

        // Assert
        verify(menuItemGateway).deleteById(negativeId);
    }

    @Test
    void execute_ShouldHandleLargeId_WhenCalled() {
        // Arrange
        var largeId = Long.MAX_VALUE;

        // Act
        deleteMenuItemUsecase.execute(largeId);

        // Assert
        verify(menuItemGateway).deleteById(largeId);
    }

    @Test
    void execute_ShouldDelegateDirectlyToGateway_WhenExecuted() {
        // Arrange
        var menuItemId = 123L;

        // Act
        deleteMenuItemUsecase.execute(menuItemId);

        // Assert
        verify(menuItemGateway).deleteById(menuItemId);
        verifyNoMoreInteractions(menuItemGateway);
    }

    @Test
    void execute_ShouldNotPerformValidation_WhenExecuted() {
        // Arrange
        var menuItemId = 999L;

        // Act
        deleteMenuItemUsecase.execute(menuItemId);

        // Assert
        verify(menuItemGateway).deleteById(menuItemId);
        verify(menuItemGateway, never()).findByMenuItemId(anyLong());
        verifyNoMoreInteractions(menuItemGateway);
    }

    @Test
    void execute_ShouldAcceptMultipleSequentialCalls_WhenExecuted() {
        // Arrange
        var firstId = 1L;
        var secondId = 2L;
        var thirdId = 3L;

        // Act
        deleteMenuItemUsecase.execute(firstId);
        deleteMenuItemUsecase.execute(secondId);
        deleteMenuItemUsecase.execute(thirdId);

        // Assert
        verify(menuItemGateway).deleteById(firstId);
        verify(menuItemGateway).deleteById(secondId);
        verify(menuItemGateway).deleteById(thirdId);
        verify(menuItemGateway, times(3)).deleteById(anyLong());
    }
}
