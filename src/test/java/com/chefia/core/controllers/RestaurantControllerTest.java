package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.restaurant.*;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;
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
class RestaurantControllerTest {

    @Mock
    private CreateRestaurantUsecase createRestaurantUsecase;

    @Mock
    private ReadAllRestauranteUsecase readAllRestauranteUsecase;

    @Mock
    private ReadRestauranteUsecase readRestauranteUsecase;

    @Mock
    private UpdateRestaurantUsecase updateRestaurantUsecase;

    @Mock
    private DeleteRestaurantUsecase deleteRestaurantUsecase;

    @InjectMocks
    private RestaurantController restaurantController;

    private CreateRestaurantDTO createRestaurantDTO;
    private UpdateRestaurantDTO updateRestaurantDTO;
    private RestaurantDTO restaurantDTO;
    private PaginatedRestaurantsDTO paginatedRestaurantsDTO;

    @BeforeEach
    void setUp() {
        createRestaurantDTO = new CreateRestaurantDTO();
        updateRestaurantDTO = new UpdateRestaurantDTO();
        restaurantDTO = new RestaurantDTO();
        paginatedRestaurantsDTO = new PaginatedRestaurantsDTO();
    }

    @Test
    void createRestaurant_ShouldReturnRestaurantDTO_WhenValidData() {
        // Arrange
        when(createRestaurantUsecase.execute(any(CreateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        var result = restaurantController.createRestaurant(createRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO, result);
        verify(createRestaurantUsecase, times(1)).execute(createRestaurantDTO);
    }

    @Test
    void getRestaurant_ShouldReturnRestaurantDTO_WhenValidId() {
        // Arrange
        var restaurantId = 1L;
        when(readRestauranteUsecase.execute(anyLong())).thenReturn(restaurantDTO);

        // Act
        var result = restaurantController.getRestaurant(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO, result);
        verify(readRestauranteUsecase, times(1)).execute(restaurantId);
    }

    @Test
    void listRestaurants_ShouldReturnPaginatedRestaurantsDTO_WhenValidParameters() {
        // Arrange
        var page = 1;
        var perPage = 10;
        when(readAllRestauranteUsecase.execute(anyInt(), anyInt())).thenReturn(paginatedRestaurantsDTO);

        // Act
        var result = restaurantController.listRestaurants(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(paginatedRestaurantsDTO, result);
        verify(readAllRestauranteUsecase, times(1)).execute(page, perPage);
    }

    @Test
    void updateRestaurant_ShouldReturnRestaurantDTO_WhenValidData() {
        // Arrange
        var restaurantId = 1L;
        when(updateRestaurantUsecase.execute(anyLong(), any(UpdateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        var result = restaurantController.updateRestaurant(restaurantId, updateRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO, result);
        verify(updateRestaurantUsecase, times(1)).execute(restaurantId, updateRestaurantDTO);
    }

    @Test
    void deleteRestaurant_ShouldCallUsecase_WhenValidId() {
        // Arrange
        var restaurantId = 1L;

        // Act
        restaurantController.deleteRestaurant(restaurantId);

        // Assert
        verify(deleteRestaurantUsecase, times(1)).execute(restaurantId);
    }
}
