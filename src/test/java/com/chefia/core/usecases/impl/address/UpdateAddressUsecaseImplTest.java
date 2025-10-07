package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAddressUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UpdateAddressUsecaseImpl updateAddressUsecase;

    private UpdateAddressDTO updateAddressDTO;
    private Address existingAddress;
    private AddressDTO addressDTO;
    private Long addressId;

    @BeforeEach
    void setUp() {
        addressId = 1L;

        updateAddressDTO = new UpdateAddressDTO();
        updateAddressDTO.setStreet("Updated Street");
        updateAddressDTO.setNumber(456);
        updateAddressDTO.setCity("Updated City");
        updateAddressDTO.setState("Updated State");
        updateAddressDTO.setCountry("Updated Country");

        existingAddress = new Address();
        existingAddress.setNrSeqAddress(addressId);
        existingAddress.setStreet("Old Street");
        existingAddress.setNumber(123);
        existingAddress.setCity("Old City");
        existingAddress.setState("Old State");
        existingAddress.setCountry("Old Country");
        existingAddress.setUserId(1L);

        addressDTO = new AddressDTO();
        addressDTO.setId(addressId);
        addressDTO.setStreet("Updated Street");
        addressDTO.setNumber(456);
        addressDTO.setCity("Updated City");
        addressDTO.setState("Updated State");
        addressDTO.setCountry("Updated Country");
    }

    @Test
    void execute_ShouldReturnUpdatedAddressDTO_WhenValidData() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.of(existingAddress));
        doNothing().when(addressGateway).updateAddress(addressId, updateAddressDTO);
        when(addressMapper.toAddressResponseDTO(existingAddress)).thenReturn(addressDTO);

        // Act
        var result = updateAddressUsecase.execute(addressId, updateAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        assertEquals("Updated Street", existingAddress.getStreet());
        assertEquals(456, existingAddress.getNumber());
        assertEquals("Updated City", existingAddress.getCity());
        assertEquals("Updated State", existingAddress.getState());
        assertEquals("Updated Country", existingAddress.getCountry());

        verify(addressGateway).findById(addressId);
        verify(addressGateway).updateAddress(addressId, updateAddressDTO);
        verify(addressMapper).toAddressResponseDTO(existingAddress);
    }

    @Test
    void execute_ShouldThrowAddressNotFoundException_WhenAddressNotFound() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(AddressNotFoundException.class,
                () -> updateAddressUsecase.execute(addressId, updateAddressDTO));

        assertEquals("Address not found with id: " + addressId, exception.getMessage());

        verify(addressGateway).findById(addressId);
        verify(addressGateway, never()).updateAddress(anyLong(), any(UpdateAddressDTO.class));
        verify(addressMapper, never()).toAddressResponseDTO(any(Address.class));
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.of(existingAddress));
        doNothing().when(addressGateway).updateAddress(addressId, updateAddressDTO);
        when(addressMapper.toAddressResponseDTO(existingAddress)).thenReturn(addressDTO);

        // Act
        updateAddressUsecase.execute(addressId, updateAddressDTO);

        // Assert
        var inOrder = inOrder(addressGateway, addressMapper);
        inOrder.verify(addressGateway).findById(addressId);
        inOrder.verify(addressGateway).updateAddress(addressId, updateAddressDTO);
        inOrder.verify(addressMapper).toAddressResponseDTO(existingAddress);
    }

    @Test
    void execute_ShouldUpdateAllAddressFields_WhenExecuted() {
        // Arrange
        when(addressGateway.findById(addressId)).thenReturn(Optional.of(existingAddress));
        doNothing().when(addressGateway).updateAddress(addressId, updateAddressDTO);
        when(addressMapper.toAddressResponseDTO(existingAddress)).thenReturn(addressDTO);

        // Act
        updateAddressUsecase.execute(addressId, updateAddressDTO);

        // Assert
        assertEquals(updateAddressDTO.getStreet(), existingAddress.getStreet());
        assertEquals(updateAddressDTO.getNumber(), existingAddress.getNumber());
        assertEquals(updateAddressDTO.getCity(), existingAddress.getCity());
        assertEquals(updateAddressDTO.getState(), existingAddress.getState());
        assertEquals(updateAddressDTO.getCountry(), existingAddress.getCountry());
    }
}

