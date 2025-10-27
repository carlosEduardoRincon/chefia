package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.entities.BusinessHours;
import com.chefia.core.entities.Restaurant;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.BusinessHoursMapper;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.restaurants.model.CreateBusinessHoursDTO;
import com.chefia.restaurants.model.CreateRestaurantDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUsecaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private BusinessHourGateway businessHourGateway;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private BusinessHoursMapper businessHoursMapper;

    @InjectMocks
    private CreateRestaurantUsecaseImpl createRestaurantUsecase;

    private CreateRestaurantDTO createRestaurantDTO;
    private Restaurant restaurant;
    private RestaurantDTO restaurantDTO;
    private List<CreateBusinessHoursDTO> businessHoursDTOList;
    private List<BusinessHours> businessHoursList;

    @BeforeEach
    void setUp() {
        businessHoursDTOList = new ArrayList<>();

        createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.setBusinessHours(businessHoursDTOList);

        restaurant = new Restaurant();
        restaurant.setNrSeqRestaurant(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setBusinessHours(Arrays.asList(new BusinessHours(), new BusinessHours()));

        restaurantDTO = new RestaurantDTO();

        businessHoursList = new ArrayList<>();
        businessHoursList.add(new BusinessHours());
        businessHoursList.add(new BusinessHours());

        createRestaurantUsecase = new CreateRestaurantUsecaseImpl(restaurantGateway, businessHourGateway, restaurantMapper, businessHoursMapper);
    }

    @Test
    void execute_ShouldReturnRestaurantDTO_WhenValidData() {
        // Arrange
        var savedId = 1L;
        when(restaurantMapper.toEntity(any(CreateRestaurantDTO.class))).thenReturn(restaurant);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedId);
        when(businessHoursMapper.toEntity(any(List.class))).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        var result = createRestaurantUsecase.execute(createRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO, result);
        verify(restaurantMapper).toEntity(createRestaurantDTO);
        verify(restaurantGateway).save(restaurant);
        verify(restaurantMapper).toRestaurantResponseDTO(restaurant);
    }

    @Test
    void execute_ShouldSaveBusinessHours_WhenRestaurantCreated() {
        // Arrange
        var savedId = 1L;
        when(restaurantMapper.toEntity(any(CreateRestaurantDTO.class))).thenReturn(restaurant);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedId);
        when(businessHoursMapper.toEntity(any(List.class))).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        createRestaurantUsecase.execute(createRestaurantDTO);

        // Assert
        verify(businessHoursMapper).toEntity(businessHoursDTOList);
        verify(businessHourGateway).save(businessHoursList, savedId);
    }

    @Test
    void execute_ShouldSetRestaurantId_WhenSaved() {
        // Arrange
        var savedId = 2L;
        Restaurant restaurantToVerify = new Restaurant();
        restaurantToVerify.setName("New Restaurant");

        when(restaurantMapper.toEntity(any(CreateRestaurantDTO.class))).thenReturn(restaurantToVerify);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedId);
        when(businessHoursMapper.toEntity(any(List.class))).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        createRestaurantUsecase.execute(createRestaurantDTO);

        // Assert
        assertEquals(savedId, restaurantToVerify.getNrSeqRestaurant());
        verify(restaurantMapper).toRestaurantResponseDTO(restaurantToVerify);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var savedId = 1L;
        when(restaurantMapper.toEntity(any(CreateRestaurantDTO.class))).thenReturn(restaurant);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedId);
        when(businessHoursMapper.toEntity(any(List.class))).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        createRestaurantUsecase.execute(createRestaurantDTO);

        // Assert
        var inOrder = inOrder(restaurantMapper, restaurantGateway, businessHoursMapper, businessHourGateway);
        inOrder.verify(restaurantMapper).toEntity(createRestaurantDTO);
        inOrder.verify(restaurantGateway).save(restaurant);
        inOrder.verify(businessHoursMapper).toEntity(businessHoursDTOList);
        inOrder.verify(businessHourGateway).save(businessHoursList, savedId);
        inOrder.verify(restaurantMapper).toRestaurantResponseDTO(restaurant);
    }

    @Test
    void execute_ShouldPassCorrectRestaurantIdToBusinessHours_WhenSaving() {
        // Arrange
        var expectedRestaurantId = 5L;
        when(restaurantMapper.toEntity(any(CreateRestaurantDTO.class))).thenReturn(restaurant);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(expectedRestaurantId);
        when(businessHoursMapper.toEntity(any(List.class))).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        createRestaurantUsecase.execute(createRestaurantDTO);

        // Assert
        verify(businessHourGateway).save(businessHoursList, expectedRestaurantId);
    }
}
