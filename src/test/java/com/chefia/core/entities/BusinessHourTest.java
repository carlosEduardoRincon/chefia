package com.chefia.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BusinessHourTest {

    private BusinessHours businessHour;

    @BeforeEach
    void setUp() {
        businessHour = new BusinessHours();
    }

    @Test
    void businessHour_ShouldInitializeWithNullValues_WhenCreatedWithDefaultConstructor() {
        // Arrange & Act
        var newBusinessHour = new BusinessHours();

        // Assert
        assertNull(newBusinessHour.getClosingTime());
        assertNull(newBusinessHour.getOpeningTime());
        assertNull(newBusinessHour.getWeekDay());
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameObject() {
        // Act & Assert
        assertEquals(businessHour, businessHour);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        // Act & Assert
        assertNotEquals(null, businessHour);
    }

}

