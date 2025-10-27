package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.entities.BusinessHours;
import com.chefia.core.entities.Restaurant;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.BusinessHoursMapper;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.restaurants.model.CreateBusinessHoursDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUsecaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private BusinessHourGateway businessHourGateway;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private BusinessHoursMapper businessHoursMapper;

    @InjectMocks
    private UpdateRestaurantUsecaseImpl updateRestaurantUsecase;

    private Restaurant existingRestaurant;
    private UpdateRestaurantDTO updateRestaurantDTO;
    private RestaurantDTO restaurantDTO;
    private List<CreateBusinessHoursDTO> businessHoursDTOList;
    private List<BusinessHours> businessHoursList;

    @BeforeEach
    void setUp() {
        existingRestaurant = new Restaurant();
        existingRestaurant.setNrSeqRestaurant(1L);
        existingRestaurant.setName("Old Restaurant Name");
        existingRestaurant.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        existingRestaurant.setActive(true);

        updateRestaurantDTO = new UpdateRestaurantDTO();
        updateRestaurantDTO.setName("Default Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(true);

        restaurantDTO = new RestaurantDTO();

        businessHoursDTOList = Arrays.asList(new CreateBusinessHoursDTO());
        businessHoursList = Arrays.asList(new BusinessHours());

        // Set default business hours for updateRestaurantDTO
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        updateRestaurantUsecase = new UpdateRestaurantUsecaseImpl(restaurantGateway, businessHourGateway, restaurantMapper, businessHoursMapper);
    }

    @Test
    void execute_ShouldReturnUpdatedRestaurantDTO_WhenRestaurantExists() {
        // Arrange
        var restaurantId = 1L;
        var newName = "Updated Restaurant Name";
        var newType = UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD;
        var newActive = false;

        updateRestaurantDTO.setName(newName);
        updateRestaurantDTO.setRestaurantType(newType);
        updateRestaurantDTO.setActive(newActive);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        var result = updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO, result);
        verify(restaurantGateway).findById(restaurantId);
        verify(restaurantGateway).update(restaurantId, existingRestaurant);
        verify(restaurantMapper).toRestaurantResponseDTO(existingRestaurant);

        // Verify that the entity was updated with new values
        assertEquals(newName, existingRestaurant.getName());
        assertEquals(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD, existingRestaurant.getRestaurantType());
        assertEquals(newActive, existingRestaurant.isActive());
        assertEquals(businessHoursList, existingRestaurant.getBusinessHours());
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenRestaurantDoesNotExist() {
        // Arrange
        var restaurantId = 999L;
        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(UserNotFoundException.class,
                    () -> updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO));

        assertEquals("Restaurant not found with id: " + restaurantId, exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
        verify(restaurantGateway, never()).update(anyLong(), any(Restaurant.class));
        verify(restaurantMapper, never()).toRestaurantResponseDTO(any(Restaurant.class));
    }

    @Test
    void execute_ShouldCallUpdateOnGateway_WhenRestaurantExists() {
        // Arrange
        var restaurantId = 1L;
        updateRestaurantDTO.setName("New Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(true);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        verify(restaurantGateway, times(1)).update(restaurantId, existingRestaurant);
    }

    @Test
    void execute_ShouldUpdateBusinessHours_WhenBusinessHoursProvided() {
        // Arrange
        var restaurantId = 1L;
        updateRestaurantDTO.setName("Test Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(true);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        verify(businessHourGateway).save(businessHoursList, restaurantId);
        verify(businessHoursMapper, times(2)).toEntity(businessHoursDTOList); // called twice in implementation
    }

    @Test
    void execute_ShouldNotUpdateBusinessHours_WhenBusinessHoursEmpty() {
        // Arrange
        var restaurantId = 1L;
        updateRestaurantDTO.setName("Test Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(true);
        updateRestaurantDTO.setBusinessHours(Arrays.asList()); // empty list

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(anyList())).thenReturn(Arrays.asList());
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        verify(businessHourGateway, never()).deleteByRestaurantIdBusinessHours(restaurantId);
        verify(businessHourGateway, never()).save(anyList(), anyLong());
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        updateRestaurantDTO.setName("Test Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(true);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(anyLong())).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        var inOrder = inOrder(restaurantGateway, businessHoursMapper, businessHourGateway, restaurantMapper);
        inOrder.verify(restaurantGateway).findById(restaurantId);
        inOrder.verify(businessHoursMapper).toEntity(businessHoursDTOList);
        inOrder.verify(businessHourGateway).deleteByRestaurantIdBusinessHours(restaurantId);
        inOrder.verify(businessHoursMapper).toEntity(businessHoursDTOList);
        inOrder.verify(businessHourGateway).save(businessHoursList, restaurantId);
        inOrder.verify(restaurantGateway).update(restaurantId, existingRestaurant);
        inOrder.verify(restaurantMapper).toRestaurantResponseDTO(existingRestaurant);
    }

    @Test
    void execute_ShouldUpdateEntityFieldsDirectly_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        var expectedName = "Direct Update Restaurant";
        var expectedType = UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD;
        var expectedActive = false;

        updateRestaurantDTO.setName(expectedName);
        updateRestaurantDTO.setRestaurantType(expectedType);
        updateRestaurantDTO.setActive(expectedActive);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        assertEquals(expectedName, existingRestaurant.getName());
        assertEquals(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD, existingRestaurant.getRestaurantType());
        assertEquals(expectedActive, existingRestaurant.isActive());
        assertEquals(businessHoursList, existingRestaurant.getBusinessHours());
    }

    @Test
    void execute_ShouldCallFindByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        updateRestaurantDTO.setName("Test Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(true);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(specificId)).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(any(Restaurant.class))).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(specificId, updateRestaurantDTO);

        // Assert
        verify(restaurantGateway).findById(specificId);
    }

    @Test
    void execute_ShouldUpdateRestaurantType_WhenTypeIsChanged() {
        // Arrange
        var restaurantId = 1L;
        var newType = UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD;
        updateRestaurantDTO.setName("Test Name");
        updateRestaurantDTO.setRestaurantType(newType);
        updateRestaurantDTO.setActive(true);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(existingRestaurant)).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        assertEquals(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD, existingRestaurant.getRestaurantType());
        verify(restaurantGateway).update(restaurantId, existingRestaurant);
        verify(restaurantMapper).toRestaurantResponseDTO(existingRestaurant);
    }

    @Test
    void execute_ShouldUpdateActiveStatus_WhenStatusIsChanged() {
        // Arrange
        var restaurantId = 1L;
        var newActiveStatus = false;
        updateRestaurantDTO.setName("Test Name");
        updateRestaurantDTO.setRestaurantType(UpdateRestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        updateRestaurantDTO.setActive(newActiveStatus);
        updateRestaurantDTO.setBusinessHours(businessHoursDTOList);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(businessHoursMapper.toEntity(businessHoursDTOList)).thenReturn(businessHoursList);
        when(restaurantMapper.toRestaurantResponseDTO(existingRestaurant)).thenReturn(restaurantDTO);

        // Act
        updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);

        // Assert
        assertEquals(newActiveStatus, existingRestaurant.isActive());
        verify(restaurantGateway).update(restaurantId, existingRestaurant);
        verify(restaurantMapper).toRestaurantResponseDTO(existingRestaurant);
    }
}
