package com.chefia.infra.web;

import com.chefia.core.controllers.RestaurantController;
import com.chefia.restaurants.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantApiControllerTest {

    @Mock
    private RestaurantController restaurantController;

    @InjectMocks
    private RestaurantApiController restaurantApiController;

    private CreateRestaurantDTO createRestaurantDTO;
    private UpdateRestaurantDTO updateRestaurantDTO;
    private RestaurantDTO restaurantDTO;
    private PaginatedRestaurantsDTO paginatedRestaurantsDTO;

    @BeforeEach
    void setUp() {
        createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.setName("Test Restaurant");

        updateRestaurantDTO = new UpdateRestaurantDTO();
        updateRestaurantDTO.setName("Updated Restaurant");

        restaurantDTO = new RestaurantDTO();
        restaurantDTO.setName("Test Restaurant");

        paginatedRestaurantsDTO = new PaginatedRestaurantsDTO();
        paginatedRestaurantsDTO.setPage(0);
        paginatedRestaurantsDTO.setPerPage(10);
        paginatedRestaurantsDTO.setTotal(1L);
    }

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurantDTO_WhenValidData() {
        // Arrange
        when(restaurantController.createRestaurant(any(CreateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        var result = restaurantApiController.createRestaurant(createRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(restaurantDTO, result.getBody());
        verify(restaurantController).createRestaurant(createRestaurantDTO);
    }

    @Test
    void createRestaurant_ShouldReturn201Status_WhenExecuted() {
        // Arrange
        when(restaurantController.createRestaurant(any(CreateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        var result = restaurantApiController.createRestaurant(createRestaurantDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void createRestaurant_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        when(restaurantController.createRestaurant(any(CreateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        restaurantApiController.createRestaurant(createRestaurantDTO);

        // Assert
        verify(restaurantController, times(1)).createRestaurant(createRestaurantDTO);
    }

    @Test
    void getRestaurant_ShouldReturnRestaurantDTO_WhenRestaurantExists() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantController.getRestaurant(anyLong())).thenReturn(restaurantDTO);

        // Act
        var result = restaurantApiController.getRestaurant(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(restaurantDTO, result.getBody());
        verify(restaurantController).getRestaurant(restaurantId);
    }

    @Test
    void getRestaurant_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(restaurantController.getRestaurant(specificId)).thenReturn(restaurantDTO);

        // Act
        restaurantApiController.getRestaurant(specificId);

        // Assert
        verify(restaurantController).getRestaurant(specificId);
    }

    @Test
    void getRestaurant_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantController.getRestaurant(restaurantId)).thenReturn(restaurantDTO);

        // Act
        var result = restaurantApiController.getRestaurant(restaurantId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void listRestaurants_ShouldReturnPaginatedRestaurantsDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(restaurantController.listRestaurants(anyInt(), anyInt())).thenReturn(paginatedRestaurantsDTO);

        // Act
        var result = restaurantApiController.listRestaurants(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(paginatedRestaurantsDTO, result.getBody());
        verify(restaurantController).listRestaurants(page, perPage);
    }

    @Test
    void listRestaurants_ShouldPassCorrectPaginationParameters_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 25;
        when(restaurantController.listRestaurants(page, perPage)).thenReturn(paginatedRestaurantsDTO);

        // Act
        restaurantApiController.listRestaurants(page, perPage);

        // Assert
        verify(restaurantController).listRestaurants(page, perPage);
    }

    @Test
    void listRestaurants_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        when(restaurantController.listRestaurants(anyInt(), anyInt())).thenReturn(paginatedRestaurantsDTO);

        // Act
        var result = restaurantApiController.listRestaurants(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurantDTO_WhenValidData() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantController.updateRestaurant(anyLong(), any(UpdateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        var result = restaurantApiController.updateRestaurant(restaurantId, updateRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(restaurantDTO, result.getBody());
        verify(restaurantController).updateRestaurant(restaurantId, updateRestaurantDTO);
    }

    @Test
    void updateRestaurant_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var restaurantId = 3L;
        var specificUpdateDTO = new UpdateRestaurantDTO();
        specificUpdateDTO.setName("Specific Name");
        when(restaurantController.updateRestaurant(restaurantId, specificUpdateDTO)).thenReturn(restaurantDTO);

        // Act
        restaurantApiController.updateRestaurant(restaurantId, specificUpdateDTO);

        // Assert
        verify(restaurantController).updateRestaurant(restaurantId, specificUpdateDTO);
    }

    @Test
    void updateRestaurant_ShouldCallControllerOnceAndReturnOkStatus_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        when(restaurantController.updateRestaurant(restaurantId, updateRestaurantDTO)).thenReturn(restaurantDTO);

        // Act
        var result = restaurantApiController.updateRestaurant(restaurantId, updateRestaurantDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(restaurantController, times(1)).updateRestaurant(restaurantId, updateRestaurantDTO);
    }

    @Test
    void deleteRestaurant_ShouldReturnNoContent_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        doNothing().when(restaurantController).deleteRestaurant(anyLong());

        // Act
        var result = restaurantApiController.deleteRestaurant(restaurantId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(restaurantController).deleteRestaurant(restaurantId);
    }

    @Test
    void deleteRestaurant_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        doNothing().when(restaurantController).deleteRestaurant(specificId);

        // Act
        restaurantApiController.deleteRestaurant(specificId);

        // Assert
        verify(restaurantController).deleteRestaurant(specificId);
    }

    @Test
    void deleteRestaurant_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        doNothing().when(restaurantController).deleteRestaurant(restaurantId);

        // Act
        restaurantApiController.deleteRestaurant(restaurantId);

        // Assert
        verify(restaurantController, times(1)).deleteRestaurant(restaurantId);
    }

    @Test
    void createRestaurant_ShouldPassCorrectDTOToController_WhenExecuted() {
        // Arrange
        var specificCreateDTO = new CreateRestaurantDTO();
        specificCreateDTO.setName("Specific Restaurant");
        when(restaurantController.createRestaurant(specificCreateDTO)).thenReturn(restaurantDTO);

        // Act
        restaurantApiController.createRestaurant(specificCreateDTO);

        // Assert
        verify(restaurantController).createRestaurant(specificCreateDTO);
    }

    @Test
    void createRestaurant_ShouldDelegateDirectlyToController_WhenExecuted() {
        // Arrange
        when(restaurantController.createRestaurant(any(CreateRestaurantDTO.class))).thenReturn(restaurantDTO);

        // Act
        restaurantApiController.createRestaurant(createRestaurantDTO);

        // Assert
        verify(restaurantController).createRestaurant(createRestaurantDTO);
        verifyNoMoreInteractions(restaurantController);
    }
}
