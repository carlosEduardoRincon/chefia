package com.chefia.core.mapper;

import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
    void toEntity_ShouldReturnAddress_WhenValidCreateAddressDTO() {
        // Act
        var result = addressMapper.toEntity(createAddressDTO);

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
    void toEntity_ShouldMapAllFields_WhenCalled() {
        // Act
        var result = addressMapper.toEntity(createAddressDTO);

        // Assert
        assertEquals("Test Street", result.getStreet());
        assertEquals(123, result.getNumber());
        assertEquals("Test City", result.getCity());
        assertEquals("Test State", result.getState());
        assertEquals("Test Country", result.getCountry());
    }

    @Test
    void toUserAddressEntity_ShouldReturnAddressWithUserId_WhenValidParameters() {
        // Act
        var result = addressMapper.toUserAddressEntity(userId, createAddressDTO);

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
    void toUserAddressEntity_ShouldSetUserId_WhenCalled() {
        // Act
        var result = addressMapper.toUserAddressEntity(userId, createAddressDTO);

        // Assert
        assertEquals(userId, result.getUserId());
        assertEquals("Test Street", result.getStreet());
        assertEquals(123, result.getNumber());
    }

    @Test
    void toRestaurantAddressEntity_ShouldReturnAddressWithRestaurantId_WhenValidParameters() {
        // Act
        var result = addressMapper.toRestaurantAddressEntity(restaurantId, createAddressDTO);

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
    void toRestaurantAddressEntity_ShouldSetRestaurantId_WhenCalled() {
        // Act
        var result = addressMapper.toRestaurantAddressEntity(restaurantId, createAddressDTO);

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
    void toUserAddressEntity_ShouldWorkWithDifferentUserIds_WhenCalled() {
        // Arrange
        Long differentUserId = 999L;

        // Act
        var result = addressMapper.toUserAddressEntity(differentUserId, createAddressDTO);

        // Assert
        assertEquals(differentUserId, result.getUserId());
        assertNull(result.getRestaurantId());
    }

    @Test
    void toRestaurantAddressEntity_ShouldWorkWithDifferentRestaurantIds_WhenCalled() {
        // Arrange
        Long differentRestaurantId = 888L;

        // Act
        var result = addressMapper.toRestaurantAddressEntity(differentRestaurantId, createAddressDTO);

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
    void toEntity_ShouldMapCorrectly_WhenValidDTO() {
        // Act
        var result = addressMapper.toEntity(createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createAddressDTO.getStreet(), result.getStreet());
        assertEquals(createAddressDTO.getNumber(), result.getNumber());
        assertEquals(createAddressDTO.getCity(), result.getCity());
        assertEquals(createAddressDTO.getState(), result.getState());
        assertEquals(createAddressDTO.getCountry(), result.getCountry());
        assertNull(result.getUserId());
        assertNull(result.getRestaurantId());
    }

    @Test
    void toUserAddressEntity_ShouldMapWithUserId_WhenValidParams() {
        // Arrange
        var userId = 1L;

        // Act
        var result = addressMapper.toUserAddressEntity(userId, createAddressDTO);

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
    void toRestaurantAddressEntity_ShouldMapWithRestaurantId_WhenValidParams() {
        // Arrange
        var restaurantId = 2L;

        // Act
        var result = addressMapper.toRestaurantAddressEntity(restaurantId, createAddressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createAddressDTO.getStreet(), result.getStreet());
        assertEquals(createAddressDTO.getNumber(), result.getNumber());
        assertEquals(createAddressDTO.getCity(), result.getCity());
        assertEquals(createAddressDTO.getState(), result.getState());
        assertEquals(createAddressDTO.getCountry(), result.getCountry());
        assertNull(result.getUserId());
        assertEquals(restaurantId, result.getRestaurantId());
    }

    @Test
    void toAddressResponseDTO_ShouldMapCorrectly_WhenValidAddress() {
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
    void toAddressResponseDTO_ShouldHandleNullId_WhenAddressIdIsNull() {
        // Arrange
        address.setNrSeqAddress(null);

        // Act
        var result = addressMapper.toAddressResponseDTO(address);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(address.getStreet(), result.getStreet());
        assertEquals(address.getNumber(), result.getNumber());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());
        assertEquals(address.getCountry(), result.getCountry());
    }

    @Test
    void toResponseListDTO_ShouldMapList_WhenValidAddressList() {
        // Arrange
        var address2 = new Address();
        address2.setNrSeqAddress(2L);
        address2.setStreet("Street 2");
        var addressList = List.of(address, address2);

        // Act
        var result = addressMapper.toResponseListDTO(addressList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(address.getNrSeqAddress(), result.get(0).getId());
        assertEquals(address2.getNrSeqAddress(), result.get(1).getId());
    }

    @Test
    void toResponseListDTO_ShouldReturnEmptyList_WhenEmptyAddressList() {
        // Arrange
        var emptyList = List.<Address>of();

        // Act
        var result = addressMapper.toResponseListDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

        @Test
        void toResponseListDTO_ShouldMapMultipleAddresses_WhenAddressListProvided() {
            // Arrange
            var address1 = new Address();
            address1.setNrSeqAddress(1L);
            address1.setStreet("Street 1");

            var address2 = new Address();
            address2.setNrSeqAddress(2L);
            address2.setStreet("Street 2");

            var addressList = List.of(address1, address2);

            // Act
            var result = addressMapper.toResponseListDTO(addressList);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(1L, result.get(0).getId());
            assertEquals("Street 1", result.get(0).getStreet());
            assertEquals(2L, result.get(1).getId());
            assertEquals("Street 2", result.get(1).getStreet());
        }

    @Test
    void mappers_ShouldCreateDistinctObjects_WhenCalled() {
        // Act
        var userAddress = addressMapper.toUserAddressEntity(userId, createAddressDTO);
        var restaurantAddress = addressMapper.toRestaurantAddressEntity(restaurantId, createAddressDTO);

        // Assert
        assertNotSame(userAddress, restaurantAddress);
        assertEquals(userId, userAddress.getUserId());
        assertEquals(restaurantId, restaurantAddress.getRestaurantId());
        assertNull(userAddress.getRestaurantId());
        assertNull(restaurantAddress.getUserId());
    }
}
