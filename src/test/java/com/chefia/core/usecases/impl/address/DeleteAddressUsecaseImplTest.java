package com.chefia.core.usecases.impl.address;

import com.chefia.core.gateway.AddressGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteAddressUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @InjectMocks
    private DeleteAddressUsecaseImpl deleteAddressUsecase;

    private Long addressId;

    @BeforeEach
    void setUp() {
        addressId = 1L;
    }

    @Test
    void execute_ShouldDeleteAddress_WhenCalled() {
        // Arrange
        doNothing().when(addressGateway).deleteById(addressId);

        // Act
        assertDoesNotThrow(() -> deleteAddressUsecase.execute(addressId));

        // Assert
        verify(addressGateway).deleteById(addressId);
    }

    @Test
    void execute_ShouldCallDeleteById_WithCorrectId() {
        // Arrange
        doNothing().when(addressGateway).deleteById(addressId);

        // Act
        deleteAddressUsecase.execute(addressId);

        // Assert
        verify(addressGateway, times(1)).deleteById(addressId);
    }

    @Test
    void execute_ShouldCallDeleteById_WithDifferentIds() {
        // Arrange
        Long addressId1 = 1L;
        Long addressId2 = 2L;
        doNothing().when(addressGateway).deleteById(any(Long.class));

        // Act
        deleteAddressUsecase.execute(addressId1);
        deleteAddressUsecase.execute(addressId2);

        // Assert
        verify(addressGateway).deleteById(addressId1);
        verify(addressGateway).deleteById(addressId2);
        verify(addressGateway, times(2)).deleteById(any(Long.class));
    }
}
