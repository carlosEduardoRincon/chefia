package com.chefia.services;

import com.chefia.entities.User;
import com.chefia.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticatorServiceTest {

    @InjectMocks
    private AuthenticatorService authenticatorService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        var login = "usuario123";
        var mockUser = new User();

        when(this.userRepository.findByLogin(login)).thenReturn(mockUser);

        var result = this.authenticatorService.loadUserByUsername(login);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(this.userRepository).findByLogin(login);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        var login = "naoexiste";

        when(this.userRepository.findByLogin(login)).thenReturn(null);

        assertNull(this.authenticatorService.loadUserByUsername(login));
    }
}
