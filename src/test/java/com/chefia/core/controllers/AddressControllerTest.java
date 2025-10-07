package com.chefia.core.controllers;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.core.usecases.interfaces.address.ReadAddressesByRestaurantUsecase;
import com.chefia.core.usecases.interfaces.address.ReadAddressesByUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private ReadAddressesByUserUsecase readAddressesByUserUsecase;

    @Mock
    private ReadAddressesByRestaurantUsecase readAddressesByRestaurantUsecase;

    @InjectMocks
    private AddressController addressController;

    private List<AddressDTO> addressList;

    @BeforeEach
    void setUp() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("Test Street");
        addressDTO.setNumber(123);
        addressList = List.of(addressDTO);
    }

    @Test
    void findByUserId_ShouldReturnAddressList_WhenValidUserId() {
        // Arrange
        var userId = 1L;
        when(readAddressesByUserUsecase.execute(anyLong())).thenReturn(addressList);

        // Act
        var result = addressController.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(addressList, result);
        verify(readAddressesByUserUsecase).execute(userId);
    }

    @Test
    void findByUserId_ShouldReturnEmptyList_WhenUserHasNoAddresses() {
        // Arrange
        var userId = 2L;
        when(readAddressesByUserUsecase.execute(userId)).thenReturn(List.of());

        // Act
        var result = addressController.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(readAddressesByUserUsecase).execute(userId);
    }

    @Test
    void findByRestaurantId_ShouldReturnAddressList_WhenValidRestaurantId() {
        // Arrange
        var restaurantId = 1L;
        when(readAddressesByRestaurantUsecase.execute(anyLong())).thenReturn(addressList);

        // Act
        var result = addressController.findByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(addressList, result);
        verify(readAddressesByRestaurantUsecase).execute(restaurantId);
    }

    @Test
    void findByRestaurantId_ShouldReturnEmptyList_WhenRestaurantHasNoAddresses() {
        // Arrange
        var restaurantId = 3L;
        when(readAddressesByRestaurantUsecase.execute(restaurantId)).thenReturn(List.of());

        // Act
        var result = addressController.findByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(readAddressesByRestaurantUsecase).execute(restaurantId);
    }

    @Test
    void findByUserId_ShouldCallUsecaseWithCorrectParameter_WhenCalled() {
        // Arrange
        var userId = 5L;
        when(readAddressesByUserUsecase.execute(userId)).thenReturn(addressList);

        // Act
        addressController.findByUserId(userId);

        // Assert
        verify(readAddressesByUserUsecase).execute(userId);
    }

    @Test
    void findByRestaurantId_ShouldCallUsecaseWithCorrectParameter_WhenCalled() {
        // Arrange
        var restaurantId = 7L;
        when(readAddressesByRestaurantUsecase.execute(restaurantId)).thenReturn(addressList);

        // Act
        addressController.findByRestaurantId(restaurantId);

        // Assert
        verify(readAddressesByRestaurantUsecase).execute(restaurantId);
    }
}