package com.chefia.core.mapper;

import com.chefia.core.entities.User;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper userMapper;

    private CreateUserDTO createUserDTO;
    private User user;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO();
        createUserDTO.setName("John Doe");
        createUserDTO.setEmail("john.doe@email.com");
        createUserDTO.setLogin("johndoe");
        createUserDTO.setPassword("password123");
        createUserDTO.setUserTypeId(1L);

        user = new User();
        user.setNrSeqUser(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@email.com");
        user.setLogin("johndoe");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
        user.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 12, 0, 0));
    }

    @Test
    void toEntity_ShouldReturnUser_WhenValidCreateUserDTO() {
        // Arrange
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(createUserDTO.getPassword())).thenReturn(encodedPassword);

        // Act
        var result = userMapper.toEntity(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createUserDTO.getName(), result.getName());
        assertEquals(createUserDTO.getEmail(), result.getEmail());
        assertEquals(createUserDTO.getLogin(), result.getLogin());
        assertEquals(encodedPassword, result.getPassword());
        assertTrue(result.isActive());
        assertNotNull(result.getCreatedAt());
        assertEquals(createUserDTO.getUserTypeId(), result.getUserTypeId());
        verify(passwordEncoder).encode(createUserDTO.getPassword());
    }

    @Test
    void toEntity_ShouldEncodePassword_WhenCalled() {
        // Arrange
        String rawPassword = "mySecretPassword";
        String encodedPassword = "encodedSecretPassword";
        createUserDTO.setPassword(rawPassword);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Act
        var result = userMapper.toEntity(createUserDTO);

        // Assert
        assertEquals(encodedPassword, result.getPassword());
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void toEntity_ShouldSetActiveToTrue_WhenCalled() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        var result = userMapper.toEntity(createUserDTO);

        // Assert
        assertTrue(result.isActive());
    }

    @Test
    void toEntity_ShouldSetCreatedAtToCurrentTime_WhenCalled() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        // Act
        var result = userMapper.toEntity(createUserDTO);

        // Assert
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertNotNull(result.getCreatedAt());
        assertTrue(result.getCreatedAt().isAfter(before));
        assertTrue(result.getCreatedAt().isBefore(after));
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenCalled() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        var result = userMapper.toEntity(createUserDTO);

        // Assert
        assertEquals(createUserDTO.getName(), result.getName());
        assertEquals(createUserDTO.getEmail(), result.getEmail());
        assertEquals(createUserDTO.getLogin(), result.getLogin());
        assertEquals(createUserDTO.getUserTypeId(), result.getUserTypeId());
    }

    @Test
    void toUserResponseDTO_ShouldReturnUserDTO_WhenValidUser() {
        // Act
        var result = userMapper.toUserResponseDTO(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getNrSeqUser(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getLogin(), result.getLogin());
        assertEquals(user.isActive(), result.isActive());
        assertEquals(user.getUserTypeId(), result.getUserTypeId());
    }

    @Test
    void toUserResponseDTO_ShouldMapDatesWithTimezone_WhenCalled() {
        // Act
        var result = userMapper.toUserResponseDTO(user);

        // Assert
        OffsetDateTime expectedCreatedAt = user.getCreatedAt().atOffset(ZoneOffset.ofHours(-3));
        OffsetDateTime expectedUpdatedAt = user.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3));

        assertEquals(expectedCreatedAt, result.getCreatedAt());
        assertEquals(expectedUpdatedAt, result.getUpdatedAt());
    }

    @Test
    void toUserResponseDTO_ShouldHandleNullUpdatedAt_WhenUpdatedAtIsNull() {
        // Arrange
        user.setUpdatedAt(null);

        // Act
        var result = userMapper.toUserResponseDTO(user);

        // Assert
        assertNull(result.getUpdatedAt());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toUserResponseDTO_ShouldNotIncludePassword_WhenCalled() {
        // Act
        var result = userMapper.toUserResponseDTO(user);

        // Assert
        assertNotNull(result);
        // UserDTO não deve incluir senha por questões de segurança
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getLogin(), result.getLogin());
    }

    @Test
    void toResponseListDTO_ShouldReturnEmptyList_WhenEmptyUserList() {
        // Arrange
        List<User> emptyList = new ArrayList<>();

        // Act
        var result = userMapper.toResponseListDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseListDTO_ShouldReturnListOfUserDTO_WhenValidUserList() {
        // Arrange
        User user2 = new User();
        user2.setNrSeqUser(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@email.com");
        user2.setLogin("janesmith");
        user2.setActive(false);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(null);

        List<User> userList = Arrays.asList(user, user2);

        // Act
        var result = userMapper.toResponseListDTO(userList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user.getNrSeqUser(), result.get(0).getId());
        assertEquals(user2.getNrSeqUser(), result.get(1).getId());
        assertEquals(user.getName(), result.get(0).getName());
        assertEquals(user2.getName(), result.get(1).getName());
    }

    @Test
    void toResponseListDTO_ShouldMapAllUsers_WhenCalled() {
        // Arrange
        User user1 = new User();
        user1.setNrSeqUser(1L);
        user1.setName("Admin User");
        user1.setEmail("admin@email.com");
        user1.setLogin("admin");
        user1.setActive(true);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUserTypeId(1L);

        User user2 = new User();
        user2.setNrSeqUser(2L);
        user2.setName("Regular User");
        user2.setEmail("user@email.com");
        user2.setLogin("user");
        user2.setActive(false);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUserTypeId(2L);

        List<User> userList = Arrays.asList(user1, user2);

        // Act
        var result = userMapper.toResponseListDTO(userList);

        // Assert
        assertEquals(2, result.size());

        UserDTO firstResult = result.get(0);
        assertEquals(1L, firstResult.getId());
        assertEquals("Admin User", firstResult.getName());
        assertEquals("admin@email.com", firstResult.getEmail());
        assertTrue(firstResult.isActive());

        UserDTO secondResult = result.get(1);
        assertEquals(2L, secondResult.getId());
        assertEquals("Regular User", secondResult.getName());
        assertEquals("user@email.com", secondResult.getEmail());
        assertFalse(secondResult.isActive());
    }

    @Test
    void toResponseListDTO_ShouldHandleNullUpdatedAtInList_WhenSomeUsersHaveNullUpdatedAt() {
        // Arrange
        User userWithoutUpdate = new User();
        userWithoutUpdate.setNrSeqUser(3L);
        userWithoutUpdate.setName("New User");
        userWithoutUpdate.setEmail("new@email.com");
        userWithoutUpdate.setLogin("newuser");
        userWithoutUpdate.setActive(true);
        userWithoutUpdate.setCreatedAt(LocalDateTime.now());
        userWithoutUpdate.setUpdatedAt(null);

        List<User> userList = Arrays.asList(user, userWithoutUpdate);

        // Act
        var result = userMapper.toResponseListDTO(userList);

        // Assert
        assertEquals(2, result.size());
        assertNotNull(result.get(0).getUpdatedAt());
        assertNull(result.get(1).getUpdatedAt());
    }

    @Test
    void toEntity_ShouldWorkWithDifferentData_WhenCalled() {
        // Arrange
        createUserDTO.setName("Different User");
        createUserDTO.setEmail("different@email.com");
        createUserDTO.setLogin("different");
        createUserDTO.setUserTypeId(3L);
        when(passwordEncoder.encode(anyString())).thenReturn("differentEncodedPassword");

        // Act
        var result = userMapper.toEntity(createUserDTO);

        // Assert
        assertEquals("Different User", result.getName());
        assertEquals("different@email.com", result.getEmail());
        assertEquals("different", result.getLogin());
        assertEquals(3L, result.getUserTypeId());
        assertEquals("differentEncodedPassword", result.getPassword());
    }

    @Test
    void toUserResponseDTO_ShouldWorkWithDifferentActiveStates_WhenCalled() {
        // Arrange
        user.setActive(false);

        // Act
        var result = userMapper.toUserResponseDTO(user);

        // Assert
        assertFalse(result.isActive());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void toEntity_ShouldCallPasswordEncoderOnce_WhenCalled() {
        // Arrange
        when(passwordEncoder.encode(createUserDTO.getPassword())).thenReturn("encodedPassword");

        // Act
        userMapper.toEntity(createUserDTO);

        // Assert
        verify(passwordEncoder, times(1)).encode(createUserDTO.getPassword());
    }

    @Test
    void toUserResponseDTO_ShouldApplyCorrectTimezone_WhenCalled() {
        // Arrange
        user.setCreatedAt(LocalDateTime.of(2023, 6, 15, 14, 30, 45));
        user.setUpdatedAt(LocalDateTime.of(2023, 6, 16, 10, 15, 20));

        // Act
        var result = userMapper.toUserResponseDTO(user);

        // Assert
        assertEquals(ZoneOffset.ofHours(-3), result.getCreatedAt().getOffset());
        assertEquals(ZoneOffset.ofHours(-3), result.getUpdatedAt().getOffset());
    }
}
