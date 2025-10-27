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
class ReadAddressesByUserUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private ReadAddressesByUserUsecaseImpl readAddressesByUserUsecase;

    private Address address;
    private AddressDTO addressDTO;
    private List<Address> addressList;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setNrSeqAddress(1L);
        address.setStreet("Test Street");
        address.setNumber(123);
        address.setCity("Test City");
        address.setState("Test State");
        address.setCountry("Test Country");
        address.setUserId(1L);

        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("Test Street");
        addressDTO.setNumber(123);
        addressDTO.setCity("Test City");
        addressDTO.setState("Test State");
        addressDTO.setCountry("Test Country");

        addressList = List.of(address);
    }

    @Test
    void execute_ShouldReturnAddressList_WhenUserHasAddresses() {
        // Arrange
        var userId = 1L;
        when(addressGateway.findByUserId(userId)).thenReturn(addressList);
        when(addressMapper.toAddressResponseDTO(any(Address.class))).thenReturn(addressDTO);

        // Act
        var result = readAddressesByUserUsecase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(addressDTO, result.get(0));
        verify(addressGateway).findByUserId(userId);
        verify(addressMapper).toAddressResponseDTO(address);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenUserHasNoAddresses() {
        // Arrange
        var userId = 2L;
        when(addressGateway.findByUserId(userId)).thenReturn(List.of());

        // Act
        var result = readAddressesByUserUsecase.execute(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressGateway).findByUserId(userId);
        verify(addressMapper, never()).toAddressResponseDTO(any(Address.class));
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectUserId_WhenExecuted() {
        // Arrange
        var userId = 5L;
        when(addressGateway.findByUserId(userId)).thenReturn(List.of());

        // Act
        readAddressesByUserUsecase.execute(userId);

        // Assert
        verify(addressGateway).findByUserId(userId);
    }

    @Test
    void execute_ShouldMapAllAddresses_WhenMultipleAddressesExist() {
        // Arrange
        var userId = 1L;
        var address2 = new Address();
        address2.setNrSeqAddress(2L);
        address2.setUserId(userId);
        var multipleAddresses = List.of(address, address2);

        when(addressGateway.findByUserId(userId)).thenReturn(multipleAddresses);
        when(addressMapper.toAddressResponseDTO(any(Address.class))).thenReturn(addressDTO);

        // Act
        var result = readAddressesByUserUsecase.execute(userId);

        // Assert
        assertEquals(2, result.size());
        verify(addressMapper, times(2)).toAddressResponseDTO(any(Address.class));
    }
}
