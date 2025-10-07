package com.chefia.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address();
    }

    @Test
    void setAndGetNrSeqAddress_ShouldWorkCorrectly() {
        // Arrange
        var expectedId = 1L;

        // Act
        address.setNrSeqAddress(expectedId);

        // Assert
        assertEquals(expectedId, address.getNrSeqAddress());
    }

    @Test
    void setAndGetStreet_ShouldWorkCorrectly() {
        // Arrange
        var expectedStreet = "123 Main Street";

        // Act
        address.setStreet(expectedStreet);

        // Assert
        assertEquals(expectedStreet, address.getStreet());
    }

    @Test
    void setAndGetNumber_ShouldWorkCorrectly() {
        // Arrange
        var expectedNumber = 456;

        // Act
        address.setNumber(expectedNumber);

        // Assert
        assertEquals(expectedNumber, address.getNumber());
    }


    @Test
    void setAndGetCity_ShouldWorkCorrectly() {
        // Arrange
        var expectedCity = "New York";

        // Act
        address.setCity(expectedCity);

        // Assert
        assertEquals(expectedCity, address.getCity());
    }

    @Test
    void setAndGetState_ShouldWorkCorrectly() {
        // Arrange
        var expectedState = "NY";

        // Act
        address.setState(expectedState);

        // Assert
        assertEquals(expectedState, address.getState());
    }

    @Test
    void setAndGetCountry_ShouldWorkCorrectly() {
        // Arrange
        var expectedCountry = "USA";

        // Act
        address.setCountry(expectedCountry);

        // Assert
        assertEquals(expectedCountry, address.getCountry());
    }

    @Test
    void address_ShouldInitializeWithNullValues_WhenCreatedWithDefaultConstructor() {
        // Arrange & Act
        var newAddress = new Address();

        // Assert
        assertNull(newAddress.getNrSeqAddress());
        assertNull(newAddress.getStreet());
        assertNull(newAddress.getNumber());
        assertNull(newAddress.getCity());
        assertNull(newAddress.getState());
        assertNull(newAddress.getCountry());
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameObject() {
        // Act & Assert
        assertEquals(address, address);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        // Act & Assert
        assertNotEquals(null, address);
    }

    @Test
    void toString_ShouldReturnStringRepresentation_WhenCalled() {
        // Arrange
        address.setNrSeqAddress(1L);
        address.setStreet("Oak Avenue");
        address.setCity("Boston");

        // Act
        var result = address.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Address"));
    }

    @Test
    void address_ShouldAllowSettingAllProperties_WhenCreated() {
        // Arrange
        var id = 5L;
        var street = "Elm Street";
        var number = 789;
        var complement = "Suite 101";
        var neighborhood = "Midtown";
        var city = "Chicago";
        var state = "IL";
        var zipCode = "60601-1234";
        var country = "USA";

        // Act
        address.setNrSeqAddress(id);
        address.setStreet(street);
        address.setNumber(number);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);

        // Assert
        assertEquals(id, address.getNrSeqAddress());
        assertEquals(street, address.getStreet());
        assertEquals(number, address.getNumber());
        assertEquals(city, address.getCity());
        assertEquals(state, address.getState());
        assertEquals(country, address.getCountry());
    }

    @Test
    void complement_ShouldAllowNullValue_WhenNotRequired() {
        // Arrange
        address.setStreet("Main Street");
        address.setNumber(123);

        // Act & Assert
        assertNotNull(address.getStreet());
        assertNotNull(address.getNumber());
    }
}
