package com.chefia.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void setAndGetNrSeqUser_ShouldWorkCorrectly() {
        // Arrange
        var expectedId = 1L;

        // Act
        user.setNrSeqUser(expectedId);

        // Assert
        assertEquals(expectedId, user.getNrSeqUser());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Arrange
        var expectedName = "John Doe";

        // Act
        user.setName(expectedName);

        // Assert
        assertEquals(expectedName, user.getName());
    }

    @Test
    void setAndGetEmail_ShouldWorkCorrectly() {
        // Arrange
        var expectedEmail = "john.doe@example.com";

        // Act
        user.setEmail(expectedEmail);

        // Assert
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    void setAndGetPassword_ShouldWorkCorrectly() {
        // Arrange
        var expectedPassword = "securePassword123";

        // Act
        user.setPassword(expectedPassword);

        // Assert
        assertEquals(expectedPassword, user.getPassword());
    }

    @Test
    void setAndGetActive_ShouldWorkCorrectly() {
        // Arrange
        var expectedStatus = true;

        // Act
        user.setActive(expectedStatus);

        // Assert
        assertEquals(expectedStatus, user.isActive());
    }

    @Test
    void user_ShouldInitializeWithNullValues_WhenCreatedWithDefaultConstructor() {
        // Arrange & Act
        var newUser = new User();

        // Assert
        assertNull(newUser.getNrSeqUser());
        assertNull(newUser.getName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameObject() {
        // Act & Assert
        assertEquals(user, user);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        // Act & Assert
        assertNotEquals(null, user);
    }

    @Test
    void toString_ShouldReturnStringRepresentation_WhenCalled() {
        // Arrange
        user.setNrSeqUser(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        // Act
        var result = user.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("User"));
    }
}
