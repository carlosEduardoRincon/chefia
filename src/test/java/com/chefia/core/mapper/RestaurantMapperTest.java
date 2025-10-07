package com.chefia.core.mapper;

import com.chefia.core.entities.BusinessHours;
import com.chefia.core.entities.Restaurant;
import com.chefia.restaurants.model.CreateBusinessHoursDTO;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantMapperTest {

    @Mock
    private BusinessHoursMapper businessHoursMapper;

    @InjectMocks
    private RestaurantMapper restaurantMapper;

    private CreateRestaurantDTO createRestaurantDTO;
    private Restaurant restaurant;
    private List<CreateBusinessHoursDTO> businessHoursDTOList;
    private List<BusinessHours> businessHoursList;
    private List<RestaurantDTO> businessHoursResponseDTOList;

    @BeforeEach
    void setUp() {
        businessHoursDTOList = new ArrayList<>();
        businessHoursList = new ArrayList<>();
        businessHoursResponseDTOList = new ArrayList<>();

        createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.setName("Test Restaurant");
        createRestaurantDTO.setRestaurantType(CreateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        createRestaurantDTO.setUserId(1L);
        createRestaurantDTO.setBusinessHours(businessHoursDTOList);

        restaurant = new Restaurant();
        restaurant.setNrSeqRestaurant(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setActive(true);
        restaurant.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
        restaurant.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 12, 0, 0));
        restaurant.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        restaurant.setBusinessHours(businessHoursList);
        restaurant.setUserId(1L);
    }

    @Test
    void toEntity_ShouldReturnRestaurant_WhenValidCreateRestaurantDTO() {
        // Arrange
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);

        // Act
        var result = restaurantMapper.toEntity(createRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createRestaurantDTO.getName(), result.getName());
        assertTrue(result.isActive());
        assertNotNull(result.getCreatedAt());
        assertEquals(createRestaurantDTO.getUserId(), result.getUserId());
        assertEquals(businessHoursList, result.getBusinessHours());
        verify(businessHoursMapper).toEntity(businessHoursDTOList);
    }

    @Test
    void toEntity_ShouldMapRestaurantType_WhenCalled() {
        // Arrange
        createRestaurantDTO.setRestaurantType(CreateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        when(businessHoursMapper.toEntity(any())).thenReturn(businessHoursList);

        // Act
        var result = restaurantMapper.toEntity(createRestaurantDTO);

        // Assert
        assertEquals(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD, result.getRestaurantType());
    }

    @Test
    void toEntity_ShouldSetDefaultActiveTrue_WhenCalled() {
        // Arrange
        when(businessHoursMapper.toEntity(any())).thenReturn(businessHoursList);

        // Act
        var result = restaurantMapper.toEntity(createRestaurantDTO);

        // Assert
        assertTrue(result.isActive());
    }

    @Test
    void toEntity_ShouldSetCreatedAtToCurrentTime_WhenCalled() {
        // Arrange
        when(businessHoursMapper.toEntity(any())).thenReturn(businessHoursList);
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        // Act
        var result = restaurantMapper.toEntity(createRestaurantDTO);

        // Assert
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertNotNull(result.getCreatedAt());
        assertTrue(result.getCreatedAt().isAfter(before));
        assertTrue(result.getCreatedAt().isBefore(after));
    }

    @Test
    void toRestaurantResponseDTO_ShouldMapDatesWithTimezone_WhenCalled() {
        // Act
        var result = restaurantMapper.toRestaurantResponseDTO(restaurant);

        // Assert
        OffsetDateTime expectedCreatedAt = restaurant.getCreatedAt().atOffset(ZoneOffset.ofHours(-3));
        OffsetDateTime expectedUpdatedAt = restaurant.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3));

        assertEquals(expectedCreatedAt, result.getCreatedAt());
        assertEquals(expectedUpdatedAt, result.getUpdatedAt());
    }


    @Test
    void toRestaurantResponseDTO_ShouldHandleNullBusinessHours_WhenBusinessHoursIsNull() {
        // Arrange
        restaurant.setBusinessHours(null);

        // Act
        var result = restaurantMapper.toRestaurantResponseDTO(restaurant);

        // Assert
        assertNull(result.getBusinessHours());
        verify(businessHoursMapper, never()).toBusinessHoursResponseDTO(any());
    }

    @Test
    void toResponseListDTO_ShouldReturnEmptyList_WhenEmptyRestaurantList() {
        // Arrange
        List<Restaurant> emptyList = new ArrayList<>();

        // Act
        var result = restaurantMapper.toResponseListDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseListDTO_ShouldReturnListOfRestaurantDTO_WhenValidRestaurantList() {
        // Arrange
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setNrSeqRestaurant(2L);
        restaurant2.setName("Restaurant 2");
        restaurant2.setActive(false);
        restaurant2.setCreatedAt(LocalDateTime.now());
        restaurant2.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        restaurant2.setBusinessHours(new ArrayList<>());
        restaurant2.setUserId(2L);

        List<Restaurant> restaurantList = Arrays.asList(restaurant, restaurant2);

        // Act
        var result = restaurantMapper.toResponseListDTO(restaurantList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(restaurant.getNrSeqRestaurant(), result.get(0).getId());
        assertEquals(restaurant2.getNrSeqRestaurant(), result.get(1).getId());
        assertEquals(restaurant.getName(), result.get(0).getName());
        assertEquals(restaurant2.getName(), result.get(1).getName());
    }

    @Test
    void toResponseListDTO_ShouldCallToRestaurantResponseDTO_ForEachRestaurant() {
        // Arrange
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setNrSeqRestaurant(1L);
        restaurant1.setName("Restaurant 1");
        restaurant1.setCreatedAt(LocalDateTime.now());
        restaurant1.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        restaurant1.setBusinessHours(new ArrayList<>());
        restaurant1.setActive(true);
        restaurant1.setUserId(1L);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setNrSeqRestaurant(2L);
        restaurant2.setName("Restaurant 2");
        restaurant2.setCreatedAt(LocalDateTime.now());
        restaurant2.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        restaurant2.setBusinessHours(new ArrayList<>());
        restaurant2.setActive(true);
        restaurant2.setUserId(2L);

        List<Restaurant> restaurantList = Arrays.asList(restaurant1, restaurant2);

        // Act
        var result = restaurantMapper.toResponseListDTO(restaurantList);

        // Assert
        verify(businessHoursMapper, times(2)).toBusinessHoursResponseDTO(anyList());
        assertEquals(2, result.size());
    }

    @Test
    void toEntity_ShouldWorkWithDifferentRestaurantTypes_WhenCalled() {
        // Arrange
        createRestaurantDTO.setRestaurantType(CreateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        when(businessHoursMapper.toEntity(any())).thenReturn(businessHoursList);

        // Act
        var result = restaurantMapper.toEntity(createRestaurantDTO);

        // Assert
        assertEquals(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD, result.getRestaurantType());
    }

    @Test
    void toRestaurantResponseDTO_ShouldMapAllRestaurantTypes_WhenCalled() {
        // Arrange
        restaurant.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);

        // Act
        var result = restaurantMapper.toRestaurantResponseDTO(restaurant);

        // Assert
        assertEquals(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD, result.getRestaurantType());
    }

    @Test
    void toRestaurantResponseDTO_ShouldReturnRestaurantDTO_WhenValidRestaurant() {
        // Act
        var result = restaurantMapper.toRestaurantResponseDTO(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(restaurant.getNrSeqRestaurant(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        assertEquals(restaurant.isActive(), result.isActive());
        assertEquals(restaurant.getRestaurantType(), result.getRestaurantType());
        assertEquals(restaurant.getUserId(), result.getUserId());
    }

    @Test
    void toRestaurantResponseDTO_ShouldHandleNullUpdatedAt_WhenUpdatedAtIsNull() {
        // Arrange
        restaurant.setUpdatedAt(null);

        // Act
        var result = restaurantMapper.toRestaurantResponseDTO(restaurant);

        // Assert
        assertNull(result.getUpdatedAt());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toRestaurantResponseDTO_ShouldApplyCorrectTimezone_WhenCalled() {
        // Arrange
        restaurant.setCreatedAt(LocalDateTime.of(2023, 6, 15, 14, 30, 45));
        restaurant.setUpdatedAt(LocalDateTime.of(2023, 6, 16, 10, 15, 20));

        // Act
        var result = restaurantMapper.toRestaurantResponseDTO(restaurant);

        // Assert
        assertEquals(ZoneOffset.ofHours(-3), result.getCreatedAt().getOffset());
        assertEquals(ZoneOffset.ofHours(-3), result.getUpdatedAt().getOffset());
    }
}
