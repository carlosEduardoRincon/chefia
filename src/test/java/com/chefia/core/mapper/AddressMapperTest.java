package com.chefia.core.mapper;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddressMapperTest {

    @InjectMocks
    private AddressMapper addressMapper;

    private CreateAddressDTO createAddressDTO;
    private Address address;
    private Long userId;
    private Long restaurantId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        restaurantId = 2L;

        createAddressDTO = new CreateAddressDTO();
        createAddressDTO.setStreet("Test Street");
        createAddressDTO.setNumber(123);
        createAddressDTO.setCity("Test City");
        createAddressDTO.setState("Test State");
        createAddressDTO.setCountry("Test Country");

        address = new Address();
        address.setNrSeqAddress(1L);
        address.setStreet("Address Street");
        address.setNumber(456);
        address.setCity("Address City");
        address.setState("Address State");
        address.setCountry("Address Country");
        address.setUserId(userId);
    }

    @Test
    void toCreateAddressEntityDefault_ShouldReturnAddress_WhenValidCreateAddressDTO() {
        // Act
        var result = addressMapper.toCreateAddressEntityDefault(createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createAddressDTO.getStreet(), result.getStreet());
        assertEquals(createAddressDTO.getNumber(), result.getNumber());
        assertEquals(createAddressDTO.getCity(), result.getCity());
        assertEquals(createAddressDTO.getState(), result.getState());
        assertEquals(createAddressDTO.getCountry(), result.getCountry());
        assertNull(result.getNrSeqAddress());
        assertNull(result.getUserId());
        assertNull(result.getRestaurantId());
    }

    @Test
    void toCreateAddressEntityDefault_ShouldMapAllFields_WhenCalled() {
        // Act
        var result = addressMapper.toCreateAddressEntityDefault(createAddressDTO);

        // Assert
        assertEquals("Test Street", result.getStreet());
        assertEquals(123, result.getNumber());
        assertEquals("Test City", result.getCity());
        assertEquals("Test State", result.getState());
        assertEquals("Test Country", result.getCountry());
    }

    @Test
    void toCreateAddressEntityToUser_ShouldReturnAddressWithUserId_WhenValidParameters() {
        // Act
        var result = addressMapper.toCreateAddressEntityToUser(userId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createAddressDTO.getStreet(), result.getStreet());
        assertEquals(createAddressDTO.getNumber(), result.getNumber());
        assertEquals(createAddressDTO.getCity(), result.getCity());
        assertEquals(createAddressDTO.getState(), result.getState());
        assertEquals(createAddressDTO.getCountry(), result.getCountry());
        assertEquals(userId, result.getUserId());
        assertNull(result.getRestaurantId());
    }

    @Test
    void toCreateAddressEntityToUser_ShouldSetUserId_WhenCalled() {
        // Act
        var result = addressMapper.toCreateAddressEntityToUser(userId, createAddressDTO);

        // Assert
        assertEquals(userId, result.getUserId());
        assertEquals("Test Street", result.getStreet());
        assertEquals(123, result.getNumber());
    }

    @Test
    void toCreateAddressEntityToRestaurant_ShouldReturnAddressWithRestaurantId_WhenValidParameters() {
        // Act
        var result = addressMapper.toCreateAddressEntityToRestaurant(restaurantId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createAddressDTO.getStreet(), result.getStreet());
        assertEquals(createAddressDTO.getNumber(), result.getNumber());
        assertEquals(createAddressDTO.getCity(), result.getCity());
        assertEquals(createAddressDTO.getState(), result.getState());
        assertEquals(createAddressDTO.getCountry(), result.getCountry());
        assertEquals(restaurantId, result.getRestaurantId());
        assertNull(result.getUserId());
    }

    @Test
    void toCreateAddressEntityToRestaurant_ShouldSetRestaurantId_WhenCalled() {
        // Act
        var result = addressMapper.toCreateAddressEntityToRestaurant(restaurantId, createAddressDTO);

        // Assert
        assertEquals(restaurantId, result.getRestaurantId());
        assertEquals("Test Street", result.getStreet());
        assertEquals(123, result.getNumber());
    }

    @Test
    void toAddressResponseDTO_ShouldReturnAddressDTO_WhenValidAddress() {
        // Act
        var result = addressMapper.toAddressResponseDTO(address);

        // Assert
        assertNotNull(result);
        assertEquals(address.getNrSeqAddress(), result.getId());
        assertEquals(address.getStreet(), result.getStreet());
        assertEquals(address.getNumber(), result.getNumber());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());
        assertEquals(address.getCountry(), result.getCountry());
    }

    @Test
    void toAddressResponseDTO_ShouldMapAllFields_WhenCalled() {
        // Act
        var result = addressMapper.toAddressResponseDTO(address);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Address Street", result.getStreet());
        assertEquals(456, result.getNumber());
        assertEquals("Address City", result.getCity());
        assertEquals("Address State", result.getState());
        assertEquals("Address Country", result.getCountry());
    }

    @Test
    void toCreateAddressEntityToUser_ShouldWorkWithDifferentUserIds_WhenCalled() {
        // Arrange
        Long differentUserId = 999L;

        // Act
        var result = addressMapper.toCreateAddressEntityToUser(differentUserId, createAddressDTO);

        // Assert
        assertEquals(differentUserId, result.getUserId());
        assertNull(result.getRestaurantId());
    }

    @Test
    void toCreateAddressEntityToRestaurant_ShouldWorkWithDifferentRestaurantIds_WhenCalled() {
        // Arrange
        Long differentRestaurantId = 888L;

        // Act
        var result = addressMapper.toCreateAddressEntityToRestaurant(differentRestaurantId, createAddressDTO);

        // Assert
        assertEquals(differentRestaurantId, result.getRestaurantId());
        assertNull(result.getUserId());
    }

    @Test
    void toAddressResponseDTO_ShouldHandleNullNrSeqAddress_WhenAddressHasNullId() {
        // Arrange
        address.setNrSeqAddress(null);

        // Act
        var result = addressMapper.toAddressResponseDTO(address);

        // Assert
        assertNull(result.getId());
        assertEquals(address.getStreet(), result.getStreet());
        assertEquals(address.getNumber(), result.getNumber());
    }

    @Test
    void mappers_ShouldCreateDistinctObjects_WhenCalled() {
        // Act
        var userAddress = addressMapper.toCreateAddressEntityToUser(userId, createAddressDTO);
        var restaurantAddress = addressMapper.toCreateAddressEntityToRestaurant(restaurantId, createAddressDTO);

        // Assert
        assertNotSame(userAddress, restaurantAddress);
        assertEquals(userId, userAddress.getUserId());
        assertEquals(restaurantId, restaurantAddress.getRestaurantId());
        assertNull(userAddress.getRestaurantId());
        assertNull(restaurantAddress.getUserId());
    }
}
