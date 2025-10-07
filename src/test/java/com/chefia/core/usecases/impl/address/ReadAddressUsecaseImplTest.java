package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.core.entities.Address;
import com.chefia.core.exceptions.AddressNotFoundException;
import com.chefia.core.gateway.AddressGateway;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadAddressUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private ReadAddressUsecaseImpl readAddressUsecase;

    private Address address;
    private AddressDTO addressDTO;
    private Long addressId;

    @BeforeEach
    void setUp() {
        addressId = 1L;

        address = new Address();
        address.setNrSeqAddress(addressId);
        address.setStreet("Test Street");
        address.setNumber(123);
        address.setCity("Test City");
        address.setState("Test State");
        address.setCountry("Test Country");
        address.setUserId(1L);

        addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street");
        addressDTO.setNumber(123);
        addressDTO.setCity("Test City");
        addressDTO.setState("Test State");
        addressDTO.setCountry("Test Country");
    }

    @Test
    void execute_ShouldReturnAddressDTO_WhenAddressExists() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressResponseDTO(address)).thenReturn(addressDTO);

        // Act
        var result = readAddressUsecase.execute(addressId);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);

        verify(addressGateway).findById(addressId);
        verify(addressMapper).toAddressResponseDTO(address);
    }

    @Test
    void execute_ShouldThrowAddressNotFoundException_WhenAddressNotFound() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(AddressNotFoundException.class, 
            () -> readAddressUsecase.execute(addressId));

        assertEquals("Address not found with id: " + addressId, exception.getMessage());

        verify(addressGateway).findById(addressId);
        verify(addressMapper, never()).toAddressResponseDTO(any(Address.class));
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressResponseDTO(address)).thenReturn(addressDTO);

        // Act
        readAddressUsecase.execute(addressId);

        // Assert
        var inOrder = inOrder(addressGateway, addressMapper);
        inOrder.verify(addressGateway).findById(addressId);
        inOrder.verify(addressMapper).toAddressResponseDTO(address);
    }
}
