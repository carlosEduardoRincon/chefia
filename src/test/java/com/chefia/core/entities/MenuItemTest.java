package com.chefia.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
    }

    @Test
    void setAndGetNrSeqMenuItem_ShouldWorkCorrectly() {
        // Arrange
        var expectedId = 1L;

        // Act
        menuItem.setNrSeqMenuItem(expectedId);

        // Assert
        assertEquals(expectedId, menuItem.getNrSeqMenuItem());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Arrange
        var expectedName = "Margherita Pizza";

        // Act
        menuItem.setName(expectedName);

        // Assert
        assertEquals(expectedName, menuItem.getName());
    }

    @Test
    void setAndGetDescription_ShouldWorkCorrectly() {
        // Arrange
        var expectedDescription = "Classic pizza with tomato, mozzarella and basil";

        // Act
        menuItem.setDescription(expectedDescription);

        // Assert
        assertEquals(expectedDescription, menuItem.getDescription());
    }

    @Test
    void setAndGetPrice_ShouldWorkCorrectly() {
        // Arrange
        var expectedPrice = Double.valueOf(12.99);

        // Act
        menuItem.setPrice(expectedPrice);

        // Assert
        assertEquals(expectedPrice, menuItem.getPrice());
    }

    @Test
    void setAndGetAvailable_ShouldWorkCorrectly() {
        // Arrange
        var expectedAvailability = true;

        // Act
        menuItem.setAvailableOnlyOnSite(expectedAvailability);

        // Assert
        assertEquals(expectedAvailability, menuItem.getAvailableOnlyOnSite());
    }

    @Test
    void setAndGetRestaurantId_ShouldWorkCorrectly() {
        // Arrange
        var expectedRestaurantId = 5L;

        // Act
        menuItem.setRestaurantId(expectedRestaurantId);

        // Assert
        assertEquals(expectedRestaurantId, menuItem.getRestaurantId());
    }

    @Test
    void menuItem_ShouldInitializeWithNullValues_WhenCreatedWithDefaultConstructor() {
        // Arrange & Act
        MenuItem newMenuItem = new MenuItem();

        // Assert
        assertNull(newMenuItem.getNrSeqMenuItem());
        assertNull(newMenuItem.getName());
        assertNull(newMenuItem.getDescription());
        assertNull(newMenuItem.getPrice());
        assertNull(newMenuItem.getAvailableOnlyOnSite());
        assertNull(newMenuItem.getRestaurantId());
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameObject() {
        // Act & Assert
        assertEquals(menuItem, menuItem);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        // Act & Assert
        assertNotEquals(null, menuItem);
    }
}
