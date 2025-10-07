package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.entities.Restaurant;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUsecaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private BusinessHourGateway businessHourGateway;

    @InjectMocks
    private DeleteRestaurantUsecaseImpl deleteRestaurantUsecase;

    @Test
    void execute_ShouldCallDeleteOnBothGateways_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        verify(restaurantGateway).deleteById(restaurantId);
    }

    @Test
    void execute_ShouldCallBusinessHoursDeleteWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 5L;

        // Act
        deleteRestaurantUsecase.execute(specificId);

        // Assert
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(specificId);
    }

    @Test
    void execute_ShouldCallRestaurantDeleteWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;

        // Act
        deleteRestaurantUsecase.execute(specificId);

        // Assert
        verify(restaurantGateway).deleteById(specificId);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId);

        // Assert
        var inOrder = inOrder(businessHourGateway, restaurantGateway);
        inOrder.verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        inOrder.verify(restaurantGateway).deleteById(restaurantId);
    }

    @Test
    void execute_ShouldCallBusinessHoursDeleteOnce_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(businessHourGateway, times(1)).deleteByRestaurantIdBusinessHours(restaurantId);
    }

    @Test
    void execute_ShouldCallRestaurantDeleteOnce_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(restaurantGateway, times(1)).deleteById(restaurantId);
    }

    @Test
    void execute_ShouldDeleteBusinessHoursFirst_WhenExecuted() {
        // Arrange
        var restaurantId = 25L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        verify(restaurantGateway).deleteById(restaurantId);

        // Verify order: business hours deleted before restaurant
        var inOrder = inOrder(businessHourGateway, restaurantGateway);
        inOrder.verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        inOrder.verify(restaurantGateway).deleteById(restaurantId);
    }

    @Test
    void execute_ShouldNotCallAnyOtherMethods_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        verify(restaurantGateway).deleteById(restaurantId);
        verifyNoMoreInteractions(businessHourGateway);
        verifyNoMoreInteractions(restaurantGateway);
    }

    @Test
    void execute_ShouldHandleDifferentRestaurantIds_WhenExecuted() {
        // Arrange
        var restaurantId1 = 10L;
        var restaurantId2 = 20L;

        // Act
        deleteRestaurantUsecase.execute(restaurantId1);
        deleteRestaurantUsecase.execute(restaurantId2);

        // Assert
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId1);
        verify(restaurantGateway).deleteById(restaurantId1);
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId2);
        verify(restaurantGateway).deleteById(restaurantId2);
    }
}
