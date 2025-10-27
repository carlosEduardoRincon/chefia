package com.chefia.infra.database.jdbc.repository;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class JdbcAddressRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<Address> mappedQuerySpec;

    @InjectMocks
    private JdbcAddressRepository addressRepository;

    private Address address;
    private UpdateAddressDTO updateAddressDTO;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setNrSeqAddress(1L);
        address.setStreet("Main Street");
        address.setNumber(123);
        address.setCity("São Paulo");
        address.setState("SP");
        address.setCountry("Brazil");
        address.setUserId(1L);

        updateAddressDTO = new UpdateAddressDTO();
        updateAddressDTO.setStreet("Updated Street");
        updateAddressDTO.setNumber(456);
        updateAddressDTO.setCity("Rio de Janeiro");
        updateAddressDTO.setState("RJ");
        updateAddressDTO.setCountry("Brazil");
    }

    @Test
    void saveAddressForUser_ShouldReturnAddressId_WhenAddressIsSaved() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_address", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        var result = addressRepository.saveUserAddress(address);

        // Assert
        assertEquals(1L, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec, times(6)).param(anyString(), any());
        verify(statementSpec).update(any(GeneratedKeyHolder.class));
    }

    @Test
    void saveAddressForUser_ShouldCallCorrectParameters_WhenSavingAddress() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_address", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        addressRepository.saveUserAddress(address);

        // Assert
        verify(statementSpec).param("street", address.getStreet());
        verify(statementSpec).param("number", address.getNumber());
        verify(statementSpec).param("city", address.getCity());
        verify(statementSpec).param("state", address.getState());
        verify(statementSpec).param("country", address.getCountry());
        verify(statementSpec).param("nr_seq_user", address.getUserId());
    }

    @Test
    void saveAddressForRestaurant_ShouldReturnAddressId_WhenAddressIsSaved() {
        // Arrange
        address.setRestaurantId(5L);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_address", 2L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        var result = addressRepository.saveRestaurantAddress(address);

        // Assert
        assertEquals(2L, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec, times(6)).param(anyString(), any());
        verify(statementSpec).update(any(GeneratedKeyHolder.class));
    }

    @Test
    void saveAddressForRestaurant_ShouldCallCorrectParameters_WhenSavingAddress() {
        // Arrange
        address.setRestaurantId(3L);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_address", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        addressRepository.saveRestaurantAddress(address);

        // Assert
        verify(statementSpec).param("street", address.getStreet());
        verify(statementSpec).param("number", address.getNumber());
        verify(statementSpec).param("city", address.getCity());
        verify(statementSpec).param("state", address.getState());
        verify(statementSpec).param("country", address.getCountry());
        verify(statementSpec).param("nr_seq_restaurant", address.getRestaurantId());
    }

    @Test
    void findById_ShouldReturnAddress_WhenAddressExists() {
        // Arrange
        var addressId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(address));

        // Act
        var result = addressRepository.findById(addressId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(address, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", addressId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findById_ShouldReturnEmpty_WhenAddressDoesNotExist() {
        // Arrange
        var addressId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = addressRepository.findById(addressId);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", addressId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void updateAddress_ShouldCallUpdateWithCorrectParameters_WhenAddressIsUpdated() {
        // Arrange
        var addressId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        addressRepository.updateAddress(addressId, updateAddressDTO);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", addressId);
        verify(statementSpec).param("street", updateAddressDTO.getStreet());
        verify(statementSpec).param("number", updateAddressDTO.getNumber());
        verify(statementSpec).param("city", updateAddressDTO.getCity());
        verify(statementSpec).param("state", updateAddressDTO.getState());
        verify(statementSpec).param("country", updateAddressDTO.getCountry());
        verify(statementSpec).update();
    }

    @Test
    void deleteById_ShouldCallDeleteWithCorrectParameter_WhenAddressIsDeleted() {
        // Arrange
        var addressId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        addressRepository.deleteById(addressId);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", addressId);
        verify(statementSpec).update();
    }

    @Test
    void saveAddressForUser_ShouldHandleDifferentUserIds_WhenSavingForDifferentUsers() {
        // Arrange
        address.setUserId(10L);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_address", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        addressRepository.saveUserAddress(address);

        // Assert
        verify(statementSpec).param("nr_seq_user", 10L);
    }

    @Test
    void saveAddressForRestaurant_ShouldHandleDifferentRestaurantIds_WhenSavingForDifferentRestaurants() {
        // Arrange
        address.setRestaurantId(25L);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_address", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        addressRepository.saveRestaurantAddress(address);

        // Assert
        verify(statementSpec).param("nr_seq_restaurant", 25L);
    }

    @Test
    void updateAddress_ShouldCallUpdateOnce_WhenUpdatingAddress() {
        // Arrange
        var addressId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        addressRepository.updateAddress(addressId, updateAddressDTO);

        // Assert
        verify(jdbcClient, times(1)).sql(anyString());
        verify(statementSpec, times(6)).param(anyString(), any());
        verify(statementSpec, times(1)).update();
    }

    @Test
    void deleteById_ShouldCallUpdateOnce_WhenDeletingAddress() {
        // Arrange
        var addressId = 5L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        addressRepository.deleteById(addressId);

        // Assert
        verify(jdbcClient, times(1)).sql(anyString());
        verify(statementSpec, times(1)).param("id", addressId);
        verify(statementSpec, times(1)).update();
    }

    @Test
    void findByUserId_ShouldReturnAddressList_WhenUserHasAddresses() {
        // Arrange
        var userId = 1L;
        var addressList = Arrays.asList(address, createSecondAddress());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(addressList);

        // Act
        var result = addressRepository.findByUserId(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(addressList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("userId", userId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findByUserId_ShouldReturnEmptyList_WhenUserHasNoAddresses() {
        // Arrange
        var userId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(List.of());

        // Act
        var result = addressRepository.findByUserId(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("userId", userId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findByUserId_ShouldCallCorrectQuery_WhenSearchingByUserId() {
        // Arrange
        var userId = 5L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(List.of());

        // Act
        addressRepository.findByUserId(userId);

        // Assert
        verify(jdbcClient).sql(contains("WHERE nr_seq_user = :userId"));
        verify(statementSpec).param("userId", userId);
    }

    @Test
    void findByRestaurantId_ShouldReturnAddressList_WhenRestaurantHasAddresses() {
        // Arrange
        var restaurantId = 2L;
        address.setRestaurantId(restaurantId);
        var addressList = Arrays.asList(address, createRestaurantAddress());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(addressList);

        // Act
        var result = addressRepository.findByRestaurantId(restaurantId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(addressList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("restaurantId", restaurantId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findByRestaurantId_ShouldReturnEmptyList_WhenRestaurantHasNoAddresses() {
        // Arrange
        var restaurantId = 888L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(List.of());

        // Act
        var result = addressRepository.findByRestaurantId(restaurantId);

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("restaurantId", restaurantId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findByRestaurantId_ShouldCallCorrectQuery_WhenSearchingByRestaurantId() {
        // Arrange
        var restaurantId = 7L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(List.of());

        // Act
        addressRepository.findByRestaurantId(restaurantId);

        // Assert
        verify(jdbcClient).sql(contains("WHERE nr_seq_restaurant = :restaurantId"));
        verify(statementSpec).param("restaurantId", restaurantId);
    }

    @Test
    void findByUserId_ShouldHandleNullResult_WhenDatabaseReturnsNull() {
        // Arrange
        var userId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(null);

        // Act
        var result = addressRepository.findByUserId(userId);

        // Assert
        assertNull(result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("userId", userId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findByRestaurantId_ShouldHandleNullResult_WhenDatabaseReturnsNull() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Address.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(null);

        // Act
        var result = addressRepository.findByRestaurantId(restaurantId);

        // Assert
        assertNull(result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("restaurantId", restaurantId);
        verify(mappedQuerySpec).list();
    }

    private Address createSecondAddress() {
        Address secondAddress = new Address();
        secondAddress.setNrSeqAddress(2L);
        secondAddress.setStreet("Second Street");
        secondAddress.setNumber(456);
        secondAddress.setCity("Rio de Janeiro");
        secondAddress.setState("RJ");
        secondAddress.setCountry("Brazil");
        secondAddress.setUserId(1L);
        return secondAddress;
    }

    private Address createRestaurantAddress() {
        Address restaurantAddress = new Address();
        restaurantAddress.setNrSeqAddress(3L);
        restaurantAddress.setStreet("Restaurant Street");
        restaurantAddress.setNumber(789);
        restaurantAddress.setCity("Belo Horizonte");
        restaurantAddress.setState("MG");
        restaurantAddress.setCountry("Brazil");
        restaurantAddress.setRestaurantId(2L);
        return restaurantAddress;
    }
}
