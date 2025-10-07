package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.exceptions.PasswordAlreadyUsed;
import com.chefia.core.exceptions.PasswordNotMatch;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.exceptions.UserNotStrongPassword;
import com.chefia.core.gateway.UserGateway;
import com.chefia.users.model.ChangePasswordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.chefia.infra.validation.annotation.StrongPasswordValidator.isValid;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserPasswordUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserPasswordUsecaseImpl updateUserPasswordUsecase;

    private User existingUser;
    private ChangePasswordDTO changePasswordDTO;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setNrSeqUser(1L);
        existingUser.setName("Test User");
        existingUser.setEmail("test@example.com");
        existingUser.setLogin("testuser");
        existingUser.setPassword("$2a$10$hashedOldPassword");
        existingUser.setActive(true);
        existingUser.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));

        changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldPassword123");
        changePasswordDTO.setNewPassword("newStrongPassword123@");

        updateUserPasswordUsecase = new UpdateUserPasswordUsecaseImpl(userGateway, passwordEncoder);
    }

    @Test
    void execute_ShouldUpdatePassword_WhenAllValidationsPass() {
        // Arrange
        var userId = 1L;

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(userId, changePasswordDTO);

            // Assert
            verify(userGateway).findById(userId);
            verify(passwordEncoder).matches(changePasswordDTO.getOldPassword(), existingUser.getPassword());
            verify(userGateway).updateUserPassword(userId, existingUser);
        }
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        var userId = 999L;
        when(userGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(UserNotFoundException.class,
                () -> updateUserPasswordUsecase.execute(userId, changePasswordDTO));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userGateway).findById(userId);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userGateway, never()).updateUserPassword(anyLong(), any(User.class));
    }

    @Test
    void execute_ShouldThrowPasswordNotMatch_WhenOldPasswordIsIncorrect() {
        // Arrange
        var userId = 1L;

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(PasswordNotMatch.class,
                () -> updateUserPasswordUsecase.execute(userId, changePasswordDTO));

        assertEquals("Wrong password", exception.getMessage());
        verify(userGateway).findById(userId);
        verify(passwordEncoder).matches(changePasswordDTO.getOldPassword(), existingUser.getPassword());
        verify(userGateway, never()).updateUserPassword(anyLong(), any(User.class));
    }

    @Test
    void execute_ShouldThrowPasswordAlreadyUsed_WhenNewPasswordEqualsOldPassword() {
        // Arrange
        var userId = 1L;
        changePasswordDTO.setNewPassword("oldPassword123"); // Same as old password

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(PasswordAlreadyUsed.class,
                () -> updateUserPasswordUsecase.execute(userId, changePasswordDTO));

        assertEquals("The passwords are equals", exception.getMessage());
        verify(userGateway).findById(userId);
        verify(passwordEncoder).matches(changePasswordDTO.getOldPassword(), existingUser.getPassword());
        verify(userGateway, never()).updateUserPassword(anyLong(), any(User.class));
    }

    @Test
    void execute_ShouldThrowUserNotStrongPassword_WhenNewPasswordIsWeak() {
        // Arrange
        var userId = 1L;
        changePasswordDTO.setNewPassword("weak");

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(false);

            // Act & Assert
            var exception = assertThrows(UserNotStrongPassword.class,
                    () -> updateUserPasswordUsecase.execute(userId, changePasswordDTO));

            assertEquals("New password not strong", exception.getMessage());
            verify(userGateway).findById(userId);
            verify(passwordEncoder).matches(changePasswordDTO.getOldPassword(), existingUser.getPassword());
            verify(userGateway, never()).updateUserPassword(anyLong(), any(User.class));
        }
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var userId = 1L;

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(userId, changePasswordDTO);

            // Assert
            var inOrder = inOrder(userGateway, passwordEncoder);
            inOrder.verify(userGateway).findById(userId);
            inOrder.verify(passwordEncoder).matches(changePasswordDTO.getOldPassword(), existingUser.getPassword());
            inOrder.verify(userGateway).updateUserPassword(userId, existingUser);
        }
    }

    @Test
    void execute_ShouldCallFindByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;

        when(userGateway.findById(specificId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(specificId, changePasswordDTO);

            // Assert
            verify(userGateway).findById(specificId);
        }
    }

    @Test
    void execute_ShouldCallPasswordEncoderWithCorrectParameters_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var specificOldPassword = "specificOldPassword";
        var specificHashedPassword = "$2a$10$specificHashedPassword";

        changePasswordDTO.setOldPassword(specificOldPassword);
        existingUser.setPassword(specificHashedPassword);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(specificOldPassword, specificHashedPassword)).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(userId, changePasswordDTO);

            // Assert
            verify(passwordEncoder).matches(specificOldPassword, specificHashedPassword);
        }
    }

    @Test
    void execute_ShouldCallUpdateUserPasswordOnGateway_WhenValidationsPass() {
        // Arrange
        var userId = 1L;

        when(userGateway.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(userId, changePasswordDTO);

            // Assert
            verify(userGateway, times(1)).updateUserPassword(userId, existingUser);
        }
    }

    @Test
    void execute_ShouldPassCorrectUserToGateway_WhenUpdatingPassword() {
        // Arrange
        var userId = 3L;
        var specificUser = new User();
        specificUser.setNrSeqUser(userId);
        specificUser.setName("Specific User");
        specificUser.setEmail("specific@example.com");
        specificUser.setPassword("$2a$10$specificHashedPassword");

        when(userGateway.findById(userId)).thenReturn(Optional.of(specificUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), specificUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(changePasswordDTO.getNewPassword())).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(userId, changePasswordDTO);

            // Assert
            verify(userGateway).updateUserPassword(userId, specificUser);
        }
    }

    @Test
    void execute_ShouldValidatePasswordStrength_WhenExecuted() {
        // Arrange
        var userId = 1L;
        var strongPassword = "StrongPassword123@";
        changePasswordDTO.setNewPassword(strongPassword);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid(strongPassword)).thenReturn(true);

            // Act
            updateUserPasswordUsecase.execute(userId, changePasswordDTO);

            // Assert
            mockedValidator.verify(() -> isValid(strongPassword), times(1));
        }
    }

    @Test
    void execute_ShouldHandleValidPasswordChange_WhenAllConditionsMet() {
        // Arrange
        var userId = 1L;
        changePasswordDTO.setOldPassword("currentPassword123");
        changePasswordDTO.setNewPassword("newValidPassword123@");

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("currentPassword123", existingUser.getPassword())).thenReturn(true);

        try (MockedStatic<com.chefia.infra.validation.annotation.StrongPasswordValidator> mockedValidator = mockStatic(com.chefia.infra.validation.annotation.StrongPasswordValidator.class)) {
            mockedValidator.when(() -> isValid("newValidPassword123@")).thenReturn(true);

            // Act
            assertDoesNotThrow(() -> updateUserPasswordUsecase.execute(userId, changePasswordDTO));

            // Assert
            verify(userGateway).findById(userId);
            verify(passwordEncoder).matches("currentPassword123", existingUser.getPassword());
            verify(userGateway).updateUserPassword(userId, existingUser);
        }
    }
}
