package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.entities.BusinessHours;
import com.chefia.core.entities.Restaurant;
import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.restaurants.model.RestaurantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadRestaurantUsecaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private BusinessHourGateway businessHourGateway;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private ReadRestaurantUsecaseImpl readRestaurantUsecase;

    private Restaurant restaurant;
    private RestaurantDTO restaurantDTO;
    private List<BusinessHours> businessHoursList;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setNrSeqRestaurant(1L);
        restaurant.setName("La Bella Italia");
        restaurant.setActive(true);

        restaurantDTO = new RestaurantDTO();

        var businessHour1 = new BusinessHours();
        var businessHour2 = new BusinessHours();
        businessHoursList = Arrays.asList(businessHour1, businessHour2);
    }

    @Test
    void execute_ShouldReturnRestaurantDTO_WhenRestaurantExists() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(businessHourGateway.findById(anyLong())).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        var result = readRestaurantUsecase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO, result);
        verify(restaurantGateway).findById(restaurantId);
        verify(businessHourGateway).findById(restaurantId);
        verify(restaurantMapper).toRestaurantResponseDTO(restaurant);
        assertEquals(businessHoursList, restaurant.getBusinessHours());
    }

    @Test
    void execute_ShouldThrowRestaurantNotFoundException_WhenRestaurantDoesNotExist() {
        // Arrange
        var restaurantId = 999L;
        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestaurantNotFoundException.class, 
                    () -> readRestaurantUsecase.execute(restaurantId));

        verify(restaurantGateway).findById(restaurantId);
        verify(businessHourGateway, never()).findById(anyLong());
        verify(restaurantMapper, never()).toRestaurantResponseDTO(any(Restaurant.class));
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(restaurantGateway.findById(specificId)).thenReturn(Optional.of(restaurant));
        when(businessHourGateway.findById(specificId)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        readRestaurantUsecase.execute(specificId);

        // Assert
        verify(restaurantGateway).findById(specificId);
        verify(businessHourGateway).findById(specificId);
    }

    @Test
    void execute_ShouldCallMapperOnce_WhenRestaurantExists() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(businessHourGateway.findById(anyLong())).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        readRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(restaurantMapper, times(1)).toRestaurantResponseDTO(restaurant);
    }

    @Test
    void execute_ShouldPassCorrectEntityToMapper_WhenMappingResponse() {
        // Arrange
        var restaurantId = 1L;
        var specificRestaurant = new Restaurant();
        specificRestaurant.setNrSeqRestaurant(restaurantId);
        specificRestaurant.setName("Pizza Palace");

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(specificRestaurant));
        when(businessHourGateway.findById(restaurantId)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(specificRestaurant)).thenReturn(restaurantDTO);

        // Act
        readRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(restaurantMapper).toRestaurantResponseDTO(specificRestaurant);
        assertEquals(businessHoursList, specificRestaurant.getBusinessHours());
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(businessHourGateway.findById(anyLong())).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        readRestaurantUsecase.execute(restaurantId);

        // Assert
        var inOrder = inOrder(restaurantGateway, businessHourGateway, restaurantMapper);
        inOrder.verify(restaurantGateway).findById(restaurantId);
        inOrder.verify(businessHourGateway).findById(restaurantId);
        inOrder.verify(restaurantMapper).toRestaurantResponseDTO(restaurant);
    }

    @Test
    void execute_ShouldSetBusinessHoursInRestaurant_WhenBusinessHoursFound() {
        // Arrange
        var restaurantId = 1L;
        var specificBusinessHours = Arrays.asList(new BusinessHours(), new BusinessHours());

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(businessHourGateway.findById(restaurantId)).thenReturn(specificBusinessHours);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        readRestaurantUsecase.execute(restaurantId);

        // Assert
        assertEquals(specificBusinessHours, restaurant.getBusinessHours());
        verify(businessHourGateway).findById(restaurantId);
    }

    @Test
    void execute_ShouldHandleEmptyBusinessHours_WhenNoBusinessHoursFound() {
        // Arrange
        var restaurantId = 1L;
        var emptyBusinessHours = new ArrayList<BusinessHours>();

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(businessHourGateway.findById(restaurantId)).thenReturn(emptyBusinessHours);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        var result = readRestaurantUsecase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(emptyBusinessHours, restaurant.getBusinessHours());
        assertTrue(restaurant.getBusinessHours().isEmpty());
        verify(businessHourGateway).findById(restaurantId);
    }

    @Test
    void execute_ShouldThrowExceptionWithCorrectMessage_WhenRestaurantNotFound() {
        // Arrange
        var restaurantId = 123L;
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(RestaurantNotFoundException.class,
                () -> readRestaurantUsecase.execute(restaurantId));

        assertEquals("Restaurant Item not found with id: " + restaurantId, exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
        verify(businessHourGateway, never()).findById(anyLong());
        verify(restaurantMapper, never()).toRestaurantResponseDTO(any(Restaurant.class));
    }
}
