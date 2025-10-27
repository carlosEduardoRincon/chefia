package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.entities.Address;
import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.gateway.UserGateway;
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
class CreateAddressForUserUsecaseImplTest {

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private CreateAddressForUserUsecaseImpl createAddressForUserUsecase;

    private CreateAddressDTO createAddressDTO;
    private User user;
    private Address address;
    private AddressDTO addressDTO;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;

        createAddressDTO = new CreateAddressDTO();
        createAddressDTO.setStreet("Test Street");
        createAddressDTO.setNumber(123);
        createAddressDTO.setCity("Test City");
        createAddressDTO.setState("Test State");
        createAddressDTO.setCountry("Test Country");

        user = new User();
        user.setNrSeqUser(userId);
        user.setName("Test User");

        address = new Address();
        address.setNrSeqAddress(1L);
        address.setUserId(userId);
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
    void execute_ShouldReturnAddressDTO_WhenValidUserIdAndData() {
        // Arrange
        var savedAddressId = 1L;
        when(userGateway.findById(userId)).thenReturn(Optional.of(user));
        when(addressMapper.toUserAddressEntity(userId, createAddressDTO)).thenReturn(address);
        when(addressGateway.saveUserAddress(address)).thenReturn(savedAddressId);
        when(addressMapper.toAddressResponseDTO(address)).thenReturn(addressDTO);

        // Act
        var result = createAddressForUserUsecase.execute(userId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO, result);
        assertEquals(savedAddressId, address.getNrSeqAddress());

        verify(userGateway).findById(userId);
        verify(addressMapper).toUserAddressEntity(userId, createAddressDTO);
        verify(addressGateway).saveUserAddress(address);
        verify(addressMapper).toAddressResponseDTO(address);
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(UserNotFoundException.class, 
            () -> createAddressForUserUsecase.execute(userId, createAddressDTO));

        assertEquals("User not found with id: " + userId, exception.getMessage());

        verify(userGateway).findById(userId);
        verify(addressMapper, never()).toUserAddressEntity(anyLong(), any(CreateAddressDTO.class));
        verify(addressGateway, never()).saveUserAddress(any(Address.class));
        verify(addressMapper, never()).toAddressResponseDTO(any(Address.class));
    }
}
