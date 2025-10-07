package com.chefia.core.controllers;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.usecases.interfaces.address.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private CreateAddressForUserUsecase createAddressForUserUsecase;

    @Mock
    private CreateAddressForRestaurantUsecase createAddressForRestaurantUsecase;

    @Mock
    private ReadAddressUsecase readAddressUsecase;

    @Mock
    private UpdateAddressUsecase updateAddressUsecase;

    @Mock
    private DeleteAddressUsecase deleteAddressUsecase;

    @InjectMocks
    private AddressController addressController;

    private CreateAddressDTO createAddressDTO;
    private UpdateAddressDTO updateAddressDTO;
    private AddressDTO addressDTO;
    private Long userId;
    private Long restaurantId;
    private Long addressId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        restaurantId = 2L;
        addressId = 3L;

        createAddressDTO = new CreateAddressDTO();
        createAddressDTO.setStreet("Test Street");
        createAddressDTO.setNumber(123);
        createAddressDTO.setCity("Test City");
        createAddressDTO.setState("Test State");
        createAddressDTO.setCountry("Test Country");

        updateAddressDTO = new UpdateAddressDTO();
        updateAddressDTO.setStreet("Updated Street");
        updateAddressDTO.setNumber(456);
        updateAddressDTO.setCity("Updated City");
        updateAddressDTO.setState("Updated State");
        updateAddressDTO.setCountry("Updated Country");

        addressDTO = new AddressDTO();
        addressDTO.setId(addressId);
        addressDTO.setStreet("Test Street");
        addressDTO.setNumber(123);
        addressDTO.setCity("Test City");
        addressDTO.setState("Test State");
        addressDTO.setCountry("Test Country");
    }

    @Test
    void createAddressForUser_ShouldReturnAddressDTO_WhenValidParameters() {
        // Arrange
        when(createAddressForUserUsecase.execute(userId, createAddressDTO)).thenReturn(addressDTO);

        // Act
        var result = addressController.createAddressForUser(userId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        verify(createAddressForUserUsecase).execute(userId, createAddressDTO);
    }

    @Test
    void createAddressForUser_ShouldCallUsecaseOnce_WhenCalled() {
        // Arrange
        when(createAddressForUserUsecase.execute(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        addressController.createAddressForUser(userId, createAddressDTO);

        // Assert
        verify(createAddressForUserUsecase, times(1)).execute(userId, createAddressDTO);
    }

    @Test
    void createAddressForRestaurant_ShouldReturnAddressDTO_WhenValidParameters() {
        // Arrange
        when(createAddressForRestaurantUsecase.execute(restaurantId, createAddressDTO)).thenReturn(addressDTO);

        // Act
        var result = addressController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        verify(createAddressForRestaurantUsecase).execute(restaurantId, createAddressDTO);
    }

    @Test
    void createAddressForRestaurant_ShouldCallUsecaseOnce_WhenCalled() {
        // Arrange
        when(createAddressForRestaurantUsecase.execute(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        addressController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        verify(createAddressForRestaurantUsecase, times(1)).execute(restaurantId, createAddressDTO);
    }

    @Test
    void findById_ShouldReturnAddressDTO_WhenValidAddressId() {
        // Arrange
        when(readAddressUsecase.execute(addressId)).thenReturn(addressDTO);

        // Act
        var result = addressController.findById(addressId);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        verify(readAddressUsecase).execute(addressId);
    }

    @Test
    void findById_ShouldCallUsecaseOnce_WhenCalled() {
        // Arrange
        when(readAddressUsecase.execute(anyLong())).thenReturn(addressDTO);

        // Act
        addressController.findById(addressId);

        // Assert
        verify(readAddressUsecase, times(1)).execute(addressId);
    }

    @Test
    void updateAddress_ShouldReturnAddressDTO_WhenValidParameters() {
        // Arrange
        when(updateAddressUsecase.execute(addressId, updateAddressDTO)).thenReturn(addressDTO);

        // Act
        var result = addressController.updateAddress(addressId, updateAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        verify(updateAddressUsecase).execute(addressId, updateAddressDTO);
    }

    @Test
    void updateAddress_ShouldCallUsecaseOnce_WhenCalled() {
        // Arrange
        when(updateAddressUsecase.execute(anyLong(), any(UpdateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        addressController.updateAddress(addressId, updateAddressDTO);

        // Assert
        verify(updateAddressUsecase, times(1)).execute(addressId, updateAddressDTO);
    }

    @Test
    void deleteAddress_ShouldCallUsecase_WhenValidAddressId() {
        // Arrange
        doNothing().when(deleteAddressUsecase).execute(addressId);

        // Act
        assertDoesNotThrow(() -> addressController.deleteAddress(addressId));

        // Assert
        verify(deleteAddressUsecase).execute(addressId);
    }

    @Test
    void deleteAddress_ShouldCallUsecaseOnce_WhenCalled() {
        // Arrange
        doNothing().when(deleteAddressUsecase).execute(anyLong());

        // Act
        addressController.deleteAddress(addressId);

        // Assert
        verify(deleteAddressUsecase, times(1)).execute(addressId);
    }

    @Test
    void deleteAddress_ShouldCallUsecaseWithDifferentIds_WhenCalledMultipleTimes() {
        // Arrange
        Long addressId1 = 1L;
        Long addressId2 = 2L;
        doNothing().when(deleteAddressUsecase).execute(any(Long.class));

        // Act
        addressController.deleteAddress(addressId1);
        addressController.deleteAddress(addressId2);

        // Assert
        verify(deleteAddressUsecase).execute(addressId1);
        verify(deleteAddressUsecase).execute(addressId2);
        verify(deleteAddressUsecase, times(2)).execute(any(Long.class));
    }

    @Test
    void createAddressForUser_ShouldPassCorrectParameters_WhenCalled() {
        // Arrange
        when(createAddressForUserUsecase.execute(userId, createAddressDTO)).thenReturn(addressDTO);

        // Act
        addressController.createAddressForUser(userId, createAddressDTO);

        // Assert
        verify(createAddressForUserUsecase).execute(eq(userId), eq(createAddressDTO));
    }

    @Test
    void createAddressForRestaurant_ShouldPassCorrectParameters_WhenCalled() {
        // Arrange
        when(createAddressForRestaurantUsecase.execute(restaurantId, createAddressDTO)).thenReturn(addressDTO);

        // Act
        addressController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        verify(createAddressForRestaurantUsecase).execute(eq(restaurantId), eq(createAddressDTO));
    }

    @Test
    void updateAddress_ShouldPassCorrectParameters_WhenCalled() {
        // Arrange
        when(updateAddressUsecase.execute(addressId, updateAddressDTO)).thenReturn(addressDTO);

        // Act
        addressController.updateAddress(addressId, updateAddressDTO);

        // Assert
        verify(updateAddressUsecase).execute(eq(addressId), eq(updateAddressDTO));
    }
}
