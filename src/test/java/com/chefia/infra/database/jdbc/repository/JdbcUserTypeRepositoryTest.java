package com.chefia.infra.database.jdbc.repository;

import com.chefia.core.entities.UserType;
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
class JdbcUserTypeRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<UserType> mappedQuerySpec;

    @InjectMocks
    private JdbcUserTypeRepository userTypeRepository;

    private UserType userType;

    @BeforeEach
    void setUp() {
        userType = new UserType();
        userType.setNrSeqUserType(1L);
        userType.setName("Admin");
        userType.setDescription("Administrator user type");
        userType.setActive(true);
        userType.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void save_ShouldReturnUserTypeId_WhenUserTypeIsSaved() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_user_type", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        var result = userTypeRepository.save(userType);

        // Assert
        assertEquals(1L, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec, times(4)).param(anyString(), any());
        verify(statementSpec).update(any(GeneratedKeyHolder.class));
    }

    @Test
    void findById_ShouldReturnUserType_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(UserType.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(userType));

        // Act
        var result = userTypeRepository.findById(userTypeId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userType, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userTypeId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findById_ShouldReturnEmpty_WhenUserTypeDoesNotExist() {
        // Arrange
        var userTypeId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(UserType.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = userTypeRepository.findById(userTypeId);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userTypeId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findByName_ShouldReturnUserType_WhenUserTypeExists() {
        // Arrange
        var name = "Admin";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(UserType.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(userType));

        // Act
        var result = userTypeRepository.findByName(name);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userType, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("name", name);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenUserTypeDoesNotExist() {
        // Arrange
        var name = "NonExistent";
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(UserType.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = userTypeRepository.findByName(name);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("name", name);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findAll_ShouldReturnUserTypeList_WhenUserTypesExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        var userTypeList = Arrays.asList(userType, new UserType());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.query(UserType.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(userTypeList);

        // Act
        var result = userTypeRepository.findAll(pageable);

        // Assert
        assertEquals(2, result.size());
        assertEquals(userTypeList, result);
        verify(jdbcClient).sql(anyString());
        verify(mappedQuerySpec).list();
    }

    @Test
    void update_ShouldCallUpdateWithCorrectParameters_WhenUserTypeIsUpdated() {
        // Arrange
        var userTypeId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        userTypeRepository.update(userTypeId, userType);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userTypeId);
        verify(statementSpec).param("name", userType.getName());
        verify(statementSpec).param("description", userType.getDescription());
        verify(statementSpec).param("active", userType.getActive());
        verify(statementSpec).param(eq("updatedAt"), any(LocalDateTime.class));
        verify(statementSpec).update();
    }

    @Test
    void deleteById_ShouldCallDeleteWithCorrectParameter_WhenUserTypeIsDeleted() {
        // Arrange
        var userTypeId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        userTypeRepository.deleteById(userTypeId);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", userTypeId);
        verify(statementSpec).update();
    }

    @Test
    void save_ShouldCallCorrectParameters_WhenSavingUserType() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_user_type", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        userTypeRepository.save(userType);

        // Assert
        verify(statementSpec).param("name", userType.getName());
        verify(statementSpec).param("description", userType.getDescription());
        verify(statementSpec).param("active", userType.getActive());
        verify(statementSpec).param("createdAt", userType.getCreatedAt());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoUserTypesExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.query(UserType.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(Arrays.asList());

        // Act
        var result = userTypeRepository.findAll(pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(mappedQuerySpec).list();
    }
}
