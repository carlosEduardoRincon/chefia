package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.core.entities.Address;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadAddressesByRestaurantUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private ReadAddressesByRestaurantUsecaseImpl readAddressesByRestaurantUsecase;

    private Address address;
    private AddressDTO addressDTO;
    private List<Address> addressList;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setNrSeqAddress(1L);
        address.setStreet("Restaurant Street");
        address.setNumber(456);
        address.setCity("Restaurant City");
        address.setState("Restaurant State");
        address.setCountry("Restaurant Country");
        address.setRestaurantId(1L);

        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("Restaurant Street");
        addressDTO.setNumber(456);
        addressDTO.setCity("Restaurant City");
        addressDTO.setState("Restaurant State");
        addressDTO.setCountry("Restaurant Country");

        addressList = List.of(address);
    }

    @Test
    void execute_ShouldReturnAddressList_WhenRestaurantHasAddresses() {
        // Arrange
        var restaurantId = 1L;
        when(addressGateway.findByRestaurantId(restaurantId)).thenReturn(addressList);
        when(addressMapper.toAddressResponseDTO(any(Address.class))).thenReturn(addressDTO);

        // Act
        var result = readAddressesByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(addressDTO, result.get(0));
        verify(addressGateway).findByRestaurantId(restaurantId);
        verify(addressMapper).toAddressResponseDTO(address);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenRestaurantHasNoAddresses() {
        // Arrange
        var restaurantId = 2L;
        when(addressGateway.findByRestaurantId(restaurantId)).thenReturn(List.of());

        // Act
        var result = readAddressesByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressGateway).findByRestaurantId(restaurantId);
        verify(addressMapper, never()).toAddressResponseDTO(any(Address.class));
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectRestaurantId_WhenExecuted() {
        // Arrange
        var restaurantId = 10L;
        when(addressGateway.findByRestaurantId(restaurantId)).thenReturn(List.of());

        // Act
        readAddressesByRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(addressGateway).findByRestaurantId(restaurantId);
    }

    @Test
    void execute_ShouldMapAllAddresses_WhenMultipleAddressesExist() {
        // Arrange
        var restaurantId = 1L;
        var address2 = new Address();
        address2.setNrSeqAddress(2L);
        address2.setRestaurantId(restaurantId);
        var multipleAddresses = List.of(address, address2);

        when(addressGateway.findByRestaurantId(restaurantId)).thenReturn(multipleAddresses);
        when(addressMapper.toAddressResponseDTO(any(Address.class))).thenReturn(addressDTO);

        // Act
        var result = readAddressesByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertEquals(2, result.size());
        verify(addressMapper, times(2)).toAddressResponseDTO(any(Address.class));
    }
}
