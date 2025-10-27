package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.entities.Restaurant;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadAllRestaurantUsecaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private ReadAllRestaurantUsecaseImpl readAllRestaurantUsecase;

    private List<Restaurant> restaurantList;
    private List<RestaurantDTO> restaurantDTOList;

    @BeforeEach
    void setUp() {
        var restaurant1 = new Restaurant();
        restaurant1.setNrSeqRestaurant(1L);
        restaurant1.setName("La Bella Italia");

        var restaurant2 = new Restaurant();
        restaurant2.setNrSeqRestaurant(2L);
        restaurant2.setName("Pizza Palace");

        restaurantList = Arrays.asList(restaurant1, restaurant2);

        var restaurantDTO1 = new RestaurantDTO();
        var restaurantDTO2 = new RestaurantDTO();
        restaurantDTOList = Arrays.asList(restaurantDTO1, restaurantDTO2);

        readAllRestaurantUsecase = new ReadAllRestaurantUsecaseImpl(restaurantGateway, restaurantMapper);
    }

    @Test
    void execute_ShouldReturnPaginatedRestaurantsDTO_WhenRestaurantsExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        var result = readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(restaurantList.size()), result.getTotal());
        assertEquals(restaurantDTOList, result.getItems());
        verify(restaurantGateway).findAll(any(Pageable.class));
        verify(restaurantMapper).toResponseListDTO(restaurantList);
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectPageable_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 5;
        var expectedPageable = PageRequest.of(page, perPage);
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        verify(restaurantGateway).findAll(expectedPageable);
    }

    @Test
    void execute_ShouldHandleEmptyList_WhenNoRestaurantsExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        var emptyList = new ArrayList<Restaurant>();
        var emptyDTOList = new ArrayList<RestaurantDTO>();
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(emptyList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(emptyDTOList);

        // Act
        var result = readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(0), result.getTotal());
        assertEquals(emptyDTOList, result.getItems());
        verify(restaurantGateway).findAll(any(Pageable.class));
        verify(restaurantMapper).toResponseListDTO(emptyList);
    }

    @Test
    void execute_ShouldPassCorrectParametersToMapper_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(restaurantList)).thenReturn(restaurantDTOList);

        // Act
        readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        verify(restaurantMapper).toResponseListDTO(restaurantList);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        var inOrder = inOrder(restaurantGateway, restaurantMapper);
        inOrder.verify(restaurantGateway).findAll(any(Pageable.class));
        inOrder.verify(restaurantMapper).toResponseListDTO(restaurantList);
    }

    @Test
    void execute_ShouldHandleDifferentPageSizes_WhenExecuted() {
        // Arrange
        var pageSizes = new Integer[]{5, 10, 25, 50};
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act & Assert
        for (var pageSize : pageSizes) {
            readAllRestaurantUsecase.execute(0, pageSize);
            verify(restaurantGateway).findAll(PageRequest.of(0, pageSize));
        }
    }

    @Test
    void execute_ShouldSetCorrectTotalFromListSize_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        var largeList = Arrays.asList(
            new Restaurant(), new Restaurant(), new Restaurant(), new Restaurant(), new Restaurant()
        );
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(largeList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        var result = readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        assertEquals(Long.valueOf(largeList.size()), result.getTotal());
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
    }

    @Test
    void execute_ShouldCreateNewPaginatedRestaurantsDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        var result = readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertInstanceOf(PaginatedRestaurantsDTO.class, result);
        verify(restaurantGateway).findAll(any(Pageable.class));
        verify(restaurantMapper).toResponseListDTO(restaurantList);
    }

    @Test
    void execute_ShouldHandleZeroPage_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 15;
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        var result = readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(restaurantGateway).findAll(PageRequest.of(page, perPage));
    }

    @Test
    void execute_ShouldHandleLargePageNumber_WhenExecuted() {
        // Arrange
        var page = 100;
        var perPage = 5;
        when(restaurantGateway.findAll(any(Pageable.class))).thenReturn(restaurantList);
        when(restaurantMapper.toResponseListDTO(anyList())).thenReturn(restaurantDTOList);

        // Act
        var result = readAllRestaurantUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(restaurantGateway).findAll(PageRequest.of(page, perPage));
    }
}
