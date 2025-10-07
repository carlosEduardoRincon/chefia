package com.chefia.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    private UserType userType;

    @BeforeEach
    void setUp() {
        userType = new UserType();
    }

    @Test
    void setAndGetNrSeqUserType_ShouldWorkCorrectly() {
        // Arrange
        var expectedId = 1L;

        // Act
        userType.setNrSeqUserType(expectedId);

        // Assert
        assertEquals(expectedId, userType.getNrSeqUserType());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Arrange
        var expectedName = "Administrator";

        // Act
        userType.setName(expectedName);

        // Assert
        assertEquals(expectedName, userType.getName());
    }

    @Test
    void setAndGetDescription_ShouldWorkCorrectly() {
        // Arrange
        var expectedDescription = "System administrator with full access";

        // Act
        userType.setDescription(expectedDescription);

        // Assert
        assertEquals(expectedDescription, userType.getDescription());
    }

    @Test
    void setAndGetActive_ShouldWorkCorrectly() {
        // Arrange
        var expectedStatus = true;

        // Act
        userType.setActive(expectedStatus);

        // Assert
        assertEquals(expectedStatus, userType.getActive());
    }

    @Test
    void userType_ShouldInitializeWithNullValues_WhenCreatedWithDefaultConstructor() {
        // Arrange & Act
        var newUserType = new UserType();

        // Assert
        assertNull(newUserType.getNrSeqUserType());
        assertNull(newUserType.getName());
        assertNull(newUserType.getDescription());
        assertNull(newUserType.getActive());
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameObject() {
        // Act & Assert
        assertEquals(userType, userType);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        // Act & Assert
        assertNotEquals(null, userType);
    }

    @Test
    void toString_ShouldReturnStringRepresentation_WhenCalled() {
        // Arrange
        userType.setNrSeqUserType(1L);
        userType.setName("Manager");
        userType.setDescription("Restaurant manager");

        // Act
        var result = userType.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("UserType"));
    }

    @Test
    void userType_ShouldAllowSettingAllProperties_WhenCreated() {
        // Arrange
        var id = 5L;
        var name = "Chef";
        var description = "Head chef of the restaurant";
        var active = false;

        // Act
        userType.setNrSeqUserType(id);
        userType.setName(name);
        userType.setDescription(description);
        userType.setActive(active);

        // Assert
        assertEquals(id, userType.getNrSeqUserType());
        assertEquals(name, userType.getName());
        assertEquals(description, userType.getDescription());
        assertEquals(active, userType.getActive());
    }
}
