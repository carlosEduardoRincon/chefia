package com.chefia.infra.validation.usertype;

import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.entities.UserType;
import com.chefia.core.exceptions.UserTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeNameValidatorTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @InjectMocks
    private UserTypeNameValidator userTypeNameValidator;

    private UserType userType;
    private UserType existingUserType;

    @BeforeEach
    void setUp() {
        userType = new UserType();
        userType.setName("Admin");
        userType.setNrSeqUserType(1L);

        existingUserType = new UserType();
        existingUserType.setName("Admin");
        existingUserType.setNrSeqUserType(2L);
    }

    @Test
    void validate_ShouldThrowUserTypeNotFoundException_WhenUserTypeAlreadyExists() {
        // Arrange
        when(userTypeGateway.findByName(anyString())).thenReturn(Optional.of(existingUserType));

        // Act & Assert
        var exception = assertThrows(
            UserTypeNotFoundException.class,
            () -> userTypeNameValidator.validate(userType)
        );

        assertEquals("User Type already exist", exception.getMessage());
        verify(userTypeGateway).findByName(userType.getName());
    }

    @Test
    void validate_ShouldNotThrowException_WhenUserTypeDoesNotExist() {
        // Arrange
        when(userTypeGateway.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> userTypeNameValidator.validate(userType));
        verify(userTypeGateway).findByName(userType.getName());
    }

    @Test
    void validate_ShouldCallFindByNameWithCorrectParameter() {
        // Arrange
        String userTypeName = "Manager";
        userType.setName(userTypeName);
        when(userTypeGateway.findByName(userTypeName)).thenReturn(Optional.empty());

        // Act
        userTypeNameValidator.validate(userType);

        // Assert
        verify(userTypeGateway).findByName(userTypeName);
    }

    @Test
    void validate_ShouldCallFindByNameOnce() {
        // Arrange
        when(userTypeGateway.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        userTypeNameValidator.validate(userType);

        // Assert
        verify(userTypeGateway, times(1)).findByName(userType.getName());
    }
}
