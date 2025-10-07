package com.chefia.infra.web;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.controllers.AddressController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressApiControllerTest {

    @Mock
    private AddressController addressController;

    @InjectMocks
    private AddressApiController addressApiController;

    private CreateAddressDTO createAddressDTO;
    private UpdateAddressDTO updateAddressDTO;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        createAddressDTO = new CreateAddressDTO();
        createAddressDTO.setStreet("Test Street");
        createAddressDTO.setNumber(123);
        createAddressDTO.setCity("Test City");

        updateAddressDTO = new UpdateAddressDTO();
        updateAddressDTO.setStreet("Updated Street");

        addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street");
        addressDTO.setNumber(123);
        addressDTO.setCity("Test City");
    }

    @Test
    void createAddressForUser_ShouldReturnCreatedAddressDTO_WhenValidData() {
        // Arrange
        var userId = 1L;
        when(addressController.createAddressForUser(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        var result = addressApiController.createAddressForUser(userId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(addressDTO, result.getBody());
        verify(addressController).createAddressForUser(userId, createAddressDTO);
    }

    @Test
    void createAddressForUser_ShouldReturn201Status_WhenExecuted() {
        // Arrange
        var userId = 1L;
        when(addressController.createAddressForUser(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        var result = addressApiController.createAddressForUser(userId, createAddressDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void createAddressForUser_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var specificUserId = 5L;
        var specificCreateDTO = new CreateAddressDTO();
        specificCreateDTO.setStreet("Specific Street");
        when(addressController.createAddressForUser(specificUserId, specificCreateDTO)).thenReturn(addressDTO);

        // Act
        addressApiController.createAddressForUser(specificUserId, specificCreateDTO);

        // Assert
        verify(addressController).createAddressForUser(specificUserId, specificCreateDTO);
    }

    @Test
    void createAddressForRestaurant_ShouldReturnCreatedAddressDTO_WhenValidData() {
        // Arrange
        var restaurantId = 1L;
        when(addressController.createAddressForRestaurant(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        var result = addressApiController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(addressDTO, result.getBody());
        verify(addressController).createAddressForRestaurant(restaurantId, createAddressDTO);
    }

    @Test
    void createAddressForRestaurant_ShouldReturn201Status_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        when(addressController.createAddressForRestaurant(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        var result = addressApiController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void createAddressForRestaurant_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var specificRestaurantId = 3L;
        var specificCreateDTO = new CreateAddressDTO();
        specificCreateDTO.setStreet("Restaurant Street");
        when(addressController.createAddressForRestaurant(specificRestaurantId, specificCreateDTO)).thenReturn(addressDTO);

        // Act
        addressApiController.createAddressForRestaurant(specificRestaurantId, specificCreateDTO);

        // Assert
        verify(addressController).createAddressForRestaurant(specificRestaurantId, specificCreateDTO);
    }

    @Test
    void getAddress_ShouldReturnAddressDTO_WhenAddressExists() {
        // Arrange
        var addressId = 1L;
        when(addressController.findById(anyLong())).thenReturn(addressDTO);

        // Act
        var result = addressApiController.getAddress(addressId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(addressDTO, result.getBody());
        verify(addressController).findById(addressId);
    }

    @Test
    void getAddress_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        when(addressController.findById(specificId)).thenReturn(addressDTO);

        // Act
        addressApiController.getAddress(specificId);

        // Assert
        verify(addressController).findById(specificId);
    }

    @Test
    void getAddress_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        var addressId = 1L;
        when(addressController.findById(addressId)).thenReturn(addressDTO);

        // Act
        var result = addressApiController.getAddress(addressId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateAddress_ShouldReturnUpdatedAddressDTO_WhenValidData() {
        // Arrange
        var addressId = 1L;
        when(addressController.updateAddress(anyLong(), any(UpdateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        var result = addressApiController.updateAddress(addressId, updateAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(addressDTO, result.getBody());
        verify(addressController).updateAddress(addressId, updateAddressDTO);
    }

    @Test
    void updateAddress_ShouldCallControllerWithCorrectParameters_WhenExecuted() {
        // Arrange
        var addressId = 2L;
        var specificUpdateDTO = new UpdateAddressDTO();
        specificUpdateDTO.setStreet("Updated Specific Street");
        when(addressController.updateAddress(addressId, specificUpdateDTO)).thenReturn(addressDTO);

        // Act
        addressApiController.updateAddress(addressId, specificUpdateDTO);

        // Assert
        verify(addressController).updateAddress(addressId, specificUpdateDTO);
    }

    @Test
    void updateAddress_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        var addressId = 1L;
        when(addressController.updateAddress(anyLong(), any(UpdateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        var result = addressApiController.updateAddress(addressId, updateAddressDTO);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateAddress_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var addressId = 1L;
        when(addressController.updateAddress(addressId, updateAddressDTO)).thenReturn(addressDTO);

        // Act
        addressApiController.updateAddress(addressId, updateAddressDTO);

        // Assert
        verify(addressController, times(1)).updateAddress(addressId, updateAddressDTO);
    }

    @Test
    void deleteAddress_ShouldReturnOkStatus_WhenExecuted() {
        // Arrange
        var addressId = 1L;
        doNothing().when(addressController).deleteAddress(anyLong());

        // Act
        var result = addressApiController.deleteAddress(addressId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
        verify(addressController).deleteAddress(addressId);
    }

    @Test
    void deleteAddress_ShouldCallControllerWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 9L;
        doNothing().when(addressController).deleteAddress(specificId);

        // Act
        addressApiController.deleteAddress(specificId);

        // Assert
        verify(addressController).deleteAddress(specificId);
    }

    @Test
    void deleteAddress_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var addressId = 1L;
        doNothing().when(addressController).deleteAddress(addressId);

        // Act
        addressApiController.deleteAddress(addressId);

        // Assert
        verify(addressController, times(1)).deleteAddress(addressId);
    }

    @Test
    void createAddressForUser_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var userId = 1L;
        when(addressController.createAddressForUser(userId, createAddressDTO)).thenReturn(addressDTO);

        // Act
        addressApiController.createAddressForUser(userId, createAddressDTO);

        // Assert
        verify(addressController, times(1)).createAddressForUser(userId, createAddressDTO);
    }

    @Test
    void createAddressForRestaurant_ShouldCallControllerOnce_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        when(addressController.createAddressForRestaurant(restaurantId, createAddressDTO)).thenReturn(addressDTO);

        // Act
        addressApiController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        verify(addressController, times(1)).createAddressForRestaurant(restaurantId, createAddressDTO);
    }

    @Test
    void createAddressForUser_ShouldDelegateDirectlyToController_WhenExecuted() {
        // Arrange
        var userId = 1L;
        when(addressController.createAddressForUser(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        addressApiController.createAddressForUser(userId, createAddressDTO);

        // Assert
        verify(addressController).createAddressForUser(userId, createAddressDTO);
        verifyNoMoreInteractions(addressController);
    }

    @Test
    void createAddressForRestaurant_ShouldDelegateDirectlyToController_WhenExecuted() {
        // Arrange
        var restaurantId = 1L;
        when(addressController.createAddressForRestaurant(anyLong(), any(CreateAddressDTO.class))).thenReturn(addressDTO);

        // Act
        addressApiController.createAddressForRestaurant(restaurantId, createAddressDTO);

        // Assert
        verify(addressController).createAddressForRestaurant(restaurantId, createAddressDTO);
        verifyNoMoreInteractions(addressController);
    }
}
