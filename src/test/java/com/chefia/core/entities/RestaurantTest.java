package com.chefia.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
    }

    @Test
    void setAndGetNrSeqRestaurant_ShouldWorkCorrectly() {
        // Arrange
        var expectedId = 1L;

        // Act
        restaurant.setNrSeqRestaurant(expectedId);

        // Assert
        assertEquals(expectedId, restaurant.getNrSeqRestaurant());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Arrange
        var expectedName = "La Bella Italia";

        // Act
        restaurant.setName(expectedName);

        // Assert
        assertEquals(expectedName, restaurant.getName());
    }

    @Test
    void setAndGetActive_ShouldWorkCorrectly() {
        // Arrange
        var expectedStatus = true;

        // Act
        restaurant.setActive(expectedStatus);

        // Assert
        assertEquals(expectedStatus, restaurant.isActive());
    }

    @Test
    void restaurant_ShouldInitializeWithNullValues_WhenCreatedWithDefaultConstructor() {
        // Arrange & Act
        var newRestaurant = new Restaurant();

        // Assert
        assertNull(newRestaurant.getNrSeqRestaurant());
        assertNull(newRestaurant.getName());
        assertNull(newRestaurant.getBusinessHours());
        assertNull(newRestaurant.getCreatedAt());
        assertNull(newRestaurant.getUpdatedAt());
        assertNull(newRestaurant.getUserId());
        assertNull(newRestaurant.getRestaurantType());
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameObject() {
        // Act & Assert
        assertEquals(restaurant, restaurant);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        // Act & Assert
        assertNotEquals(null, restaurant);
    }

    @Test
    void toString_ShouldReturnStringRepresentation_WhenCalled() {
        // Arrange
        restaurant.setNrSeqRestaurant(1L);
        restaurant.setName("Test Restaurant");

        // Act
        var result = restaurant.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Restaurant"));
    }
}
