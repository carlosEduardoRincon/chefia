package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.entities.Address;
import com.chefia.core.entities.Restaurant;
import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAddressForRestaurantUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private CreateAddressForRestaurantUsecaseImpl createAddressForRestaurantUsecase;

    private CreateAddressDTO createAddressDTO;
    private Restaurant restaurant;
    private Address address;
    private AddressDTO addressDTO;
    private Long restaurantId;

    @BeforeEach
    void setUp() {
        restaurantId = 1L;

        createAddressDTO = new CreateAddressDTO();
        createAddressDTO.setStreet("Test Street");
        createAddressDTO.setNumber(123);
        createAddressDTO.setCity("Test City");
        createAddressDTO.setState("Test State");
        createAddressDTO.setCountry("Test Country");

        restaurant = new Restaurant();
        restaurant.setNrSeqRestaurant(restaurantId);
        restaurant.setName("Test Restaurant");

        address = new Address();
        address.setNrSeqAddress(1L);
        address.setRestaurantId(restaurantId);
        address.setStreet("Test Street");
        address.setNumber(123);
        address.setCity("Test City");
        address.setState("Test State");
        address.setCountry("Test Country");

        addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street");
        addressDTO.setNumber(123);
        addressDTO.setCity("Test City");
        addressDTO.setState("Test State");
        addressDTO.setCountry("Test Country");
    }

    @Test
    void execute_ShouldReturnAddressDTO_WhenValidRestaurantIdAndData() {
        // Arrange
        var savedAddressId = 1L;
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(addressMapper.toRestaurantAddressEntity(restaurantId, createAddressDTO)).thenReturn(address);
        when(addressGateway.saveRestaurantAddress(address)).thenReturn(savedAddressId);
        when(addressMapper.toAddressResponseDTO(address)).thenReturn(addressDTO);

        // Act
        var result = createAddressForRestaurantUsecase.execute(restaurantId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        assertEquals(savedAddressId, address.getNrSeqAddress());

        verify(restaurantGateway).findById(restaurantId);
        verify(addressMapper).toRestaurantAddressEntity(restaurantId, createAddressDTO);
        verify(addressGateway).saveRestaurantAddress(address);
        verify(addressMapper).toAddressResponseDTO(address);
    }

    @Test
    void execute_ShouldThrowRestaurantNotFoundException_WhenRestaurantNotFound() {
        // Arrange
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(RestaurantNotFoundException.class, 
            () -> createAddressForRestaurantUsecase.execute(restaurantId, createAddressDTO));

        assertEquals("Restaurant not found with id: " + restaurantId, exception.getMessage());

        verify(restaurantGateway).findById(restaurantId);
        verify(addressMapper, never()).toRestaurantAddressEntity(anyLong(), any(CreateAddressDTO.class));
        verify(addressGateway, never()).saveRestaurantAddress(any(Address.class));
        verify(addressMapper, never()).toAddressResponseDTO(any(Address.class));
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var savedAddressId = 1L;
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(addressMapper.toRestaurantAddressEntity(restaurantId, createAddressDTO)).thenReturn(address);
        when(addressGateway.saveRestaurantAddress(address)).thenReturn(savedAddressId);
        when(addressMapper.toAddressResponseDTO(address)).thenReturn(addressDTO);

        // Act
        createAddressForRestaurantUsecase.execute(restaurantId, createAddressDTO);

        // Assert
        var inOrder = inOrder(restaurantGateway, addressMapper, addressGateway);
        inOrder.verify(restaurantGateway).findById(restaurantId);
        inOrder.verify(addressMapper).toRestaurantAddressEntity(restaurantId, createAddressDTO);
        inOrder.verify(addressGateway).saveRestaurantAddress(address);
        inOrder.verify(addressMapper).toAddressResponseDTO(address);
    }
}
