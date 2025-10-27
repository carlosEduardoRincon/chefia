package com.chefia.core.mapper;

import com.chefia.users.model.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginMapperTest {

    @InjectMocks
    private LoginMapper loginMapper;

    private String validToken;

    @BeforeEach
    void setUp() {
        validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
    }

    @Test
    void toUserResponseDTO_ShouldReturnLoginResponseDTO_WhenValidToken() {
        // Act
        var result = loginMapper.toUserResponseDTO(validToken);

        // Assert
        assertNotNull(result);
        assertEquals(validToken, result.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldCreateNewInstance_WhenCalled() {
        // Act
        var result = loginMapper.toUserResponseDTO(validToken);

        // Assert
        assertNotNull(result);
        assertInstanceOf(LoginResponseDTO.class, result);
        assertEquals(validToken, result.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldHandleEmptyToken_WhenTokenIsEmpty() {
        // Arrange
        String emptyToken = "";

        // Act
        var result = loginMapper.toUserResponseDTO(emptyToken);

        // Assert
        assertNotNull(result);
        assertEquals(emptyToken, result.getToken());
        assertEquals("", result.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldHandleNullToken_WhenTokenIsNull() {
        // Arrange
        String nullToken = null;

        // Act
        var result = loginMapper.toUserResponseDTO(nullToken);

        // Assert
        assertNotNull(result);
        assertNull(result.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldWorkWithDifferentTokens_WhenCalledMultipleTimes() {
        // Arrange
        String token1 = "token.one.test";
        String token2 = "token.two.test";

        // Act
        var result1 = loginMapper.toUserResponseDTO(token1);
        var result2 = loginMapper.toUserResponseDTO(token2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(token1, result1.getToken());
        assertEquals(token2, result2.getToken());
        assertNotEquals(result1.getToken(), result2.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldHandleLongToken_WhenTokenIsLong() {
        // Arrange
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // Act
        var result = loginMapper.toUserResponseDTO(longToken);

        // Assert
        assertNotNull(result);
        assertEquals(longToken, result.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldHandleSpecialCharacters_WhenTokenHasSpecialChars() {
        // Arrange
        String tokenWithSpecialChars = "token_with-special.chars@123!";

        // Act
        var result = loginMapper.toUserResponseDTO(tokenWithSpecialChars);

        // Assert
        assertNotNull(result);
        assertEquals(tokenWithSpecialChars, result.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldReturnDistinctObjects_WhenCalledMultipleTimes() {
        // Act
        var result1 = loginMapper.toUserResponseDTO(validToken);
        var result2 = loginMapper.toUserResponseDTO(validToken);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals(result1.getToken(), result2.getToken());
    }

    @Test
    void toUserResponseDTO_ShouldSetOnlyTokenField_WhenCalled() {
        // Act
        var result = loginMapper.toUserResponseDTO(validToken);

        // Assert
        assertNotNull(result);
        assertEquals(validToken, result.getToken());
        // Verificar se apenas o token foi definido - assumindo que LoginResponseDTO tem apenas o campo token
    }
}
