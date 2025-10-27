package com.chefia.infra.database.jdbc.repository;

import com.chefia.core.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcUserRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<User> mappedQuerySpec;

    @Mock
    private JdbcClient.MappedQuerySpec<Integer> integerMappedQuerySpec;

    @InjectMocks
    private JdbcUserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setNrSeqUser(1L);
        user.setName("John Doe");
        user.setEmail("john@email.com");
        user.setLogin("johndoe");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUserTypeId(1L);
    }

    @Test
    void save_ShouldReturnUserId_WhenUserIsSaved() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_user", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        var result = userRepository.save(user);

        // Assert
        assertEquals(1L, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec, times(8)).param(anyString(), any());
        verify(statementSpec).update(any(GeneratedKeyHolder.class));
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        var userId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(User.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(user));

        // Act
        var result = userRepository.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // Arrange
        var userId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(User.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = userRepository.findById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findByLogin_ShouldReturnUser_WhenUserExists() {
        // Arrange
        var login = "johndoe";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(User.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(user));

        // Act
        var result = userRepository.findByLogin(login);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("login", login);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findByLogin_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // Arrange
        var login = "nonexistent";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(User.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = userRepository.findByLogin(login);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("login", login);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findAll_ShouldReturnUserList_WhenUsersExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        var userList = Arrays.asList(user, new User());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(User.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(userList);

        // Act
        var result = userRepository.findAll(pageable);

        // Assert
        assertEquals(2, result.size());
        assertEquals(userList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("size", pageable.getPageSize());
        verify(statementSpec).param("offset", pageable.getOffset());
        verify(mappedQuerySpec).list();
    }

    @Test
    void update_ShouldCallUpdateWithCorrectParameters_WhenUserIsUpdated() {
        // Arrange
        var userId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        userRepository.update(userId, user);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userId);
        verify(statementSpec).param("name", user.getName());
        verify(statementSpec).param("email", user.getEmail());
        verify(statementSpec).param("login", user.getLogin());
        verify(statementSpec).param(eq("updatedAt"), any(LocalDateTime.class));
        verify(statementSpec).update();
    }

    @Test
    void deleteById_ShouldCallDeleteWithCorrectParameter_WhenUserIsDeleted() {
        // Arrange
        var userId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        userRepository.deleteById(userId);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userId);
        verify(statementSpec).update();
    }

    @Test
    void updateUserStatus_ShouldCallUpdateWithCorrectParameters_WhenStatusIsUpdated() {
        // Arrange
        var userId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        userRepository.updateUserStatus(userId, user);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userId);
        verify(statementSpec).param("active", user.isActive());
        verify(statementSpec).update();
    }

    @Test
    void updateUserPassword_ShouldCallUpdateWithCorrectParameters_WhenPasswordIsUpdated() {
        // Arrange
        var userId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        userRepository.updateUserPassword(userId, user);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userId);
        verify(statementSpec).param("password", user.getPassword());
        verify(statementSpec).update();
    }

    @Test
    void findByEmailValidation_ShouldReturnTrue_WhenEmailExists() {
        // Arrange
        var email = "john@email.com";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerMappedQuerySpec);
        when(integerMappedQuerySpec.single()).thenReturn(1);

        // Act
        var result = userRepository.findByEmailValidation(email);

        // Assert
        assertTrue(result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("email", email);
        verify(integerMappedQuerySpec).single();
    }

    @Test
    void findByEmailValidation_ShouldReturnFalse_WhenEmailDoesNotExist() {
        // Arrange
        var email = "nonexistent@email.com";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerMappedQuerySpec);
        when(integerMappedQuerySpec.single()).thenReturn(0);

        // Act
        var result = userRepository.findByEmailValidation(email);

        // Assert
        assertFalse(result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("email", email);
        verify(integerMappedQuerySpec).single();
    }

    @Test
    void findByLoginValidation_ShouldReturnTrue_WhenLoginExists() {
        // Arrange
        var login = "johndoe";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerMappedQuerySpec);
        when(integerMappedQuerySpec.single()).thenReturn(1);

        // Act
        var result = userRepository.findByLoginValidation(login);

        // Assert
        assertTrue(result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("login", login);
        verify(integerMappedQuerySpec).single();
    }

    @Test
    void findByLoginValidation_ShouldReturnFalse_WhenLoginDoesNotExist() {
        // Arrange
        var login = "nonexistent";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerMappedQuerySpec);
        when(integerMappedQuerySpec.single()).thenReturn(0);

        // Act
        var result = userRepository.findByLoginValidation(login);

        // Assert
        assertFalse(result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("login", login);
        verify(integerMappedQuerySpec).single();
    }
}
