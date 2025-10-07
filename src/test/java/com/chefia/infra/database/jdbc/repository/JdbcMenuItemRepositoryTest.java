package com.chefia.infra.database.jdbc.repository;

import com.chefia.core.entities.MenuItem;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcMenuItemRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<MenuItem> mappedQuerySpec;

    @InjectMocks
    private JdbcMenuItemRepository menuItemRepository;

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        menuItem.setNrSeqMenuItem(1L);
        menuItem.setName("Burger");
        menuItem.setDescription("Delicious burger");
        menuItem.setPrice(15.99);
        menuItem.setAvailableOnlyOnSite(true);
        menuItem.setImagePath("/images/burger.jpg");
        menuItem.setRestaurantId(1L);
    }

    @Test
    void save_ShouldReturnMenuItemId_WhenMenuItemIsSaved() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_menu_item", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        var result = menuItemRepository.save(menuItem);

        // Assert
        assertEquals(1L, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec, times(6)).param(anyString(), any());
        verify(statementSpec).update(any(GeneratedKeyHolder.class));
    }

    @Test
    void findByMenuItemId_ShouldReturnMenuItem_WhenMenuItemExists() {
        // Arrange
        var menuItemId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(MenuItem.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(menuItem));

        // Act
        var result = menuItemRepository.findByMenuItemId(menuItemId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(menuItem, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", menuItemId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findByMenuItemId_ShouldReturnEmpty_WhenMenuItemDoesNotExist() {
        // Arrange
        var menuItemId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(MenuItem.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = menuItemRepository.findByMenuItemId(menuItemId);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", menuItemId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findByRestaurantId_ShouldReturnMenuItemList_WhenMenuItemsExist() {
        // Arrange
        var restaurantId = 1L;
        var menuItemList = Arrays.asList(menuItem, new MenuItem());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(MenuItem.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(menuItemList);

        // Act
        var result = menuItemRepository.findByRestaurantId(restaurantId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(menuItemList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("restaurantId", restaurantId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findAll_ShouldReturnMenuItemList_WhenMenuItemsExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        var menuItemList = Arrays.asList(menuItem, new MenuItem());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(MenuItem.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(menuItemList);

        // Act
        var result = menuItemRepository.findAll(pageable);

        // Assert
        assertEquals(2, result.size());
        assertEquals(menuItemList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("size", pageable.getPageSize());
        verify(statementSpec).param("offset", pageable.getOffset());
        verify(mappedQuerySpec).list();
    }

    @Test
    void update_ShouldCallUpdateWithCorrectParameters_WhenMenuItemIsUpdated() {
        // Arrange
        var menuItemId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        menuItemRepository.update(menuItemId, menuItem);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", menuItemId);
        verify(statementSpec).param("name", menuItem.getName());
        verify(statementSpec).param("description", menuItem.getDescription());
        verify(statementSpec).param("price", menuItem.getPrice());
        verify(statementSpec).param("availableOnlyOnSite", menuItem.getAvailableOnlyOnSite());
        verify(statementSpec).param("imagePath", menuItem.getImagePath());
        verify(statementSpec).update();
    }

    @Test
    void deleteById_ShouldCallDeleteWithCorrectParameter_WhenMenuItemIsDeleted() {
        // Arrange
        var menuItemId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        menuItemRepository.deleteById(menuItemId);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", menuItemId);
        verify(statementSpec).update();
    }

    @Test
    void save_ShouldCallCorrectParameters_WhenSavingMenuItem() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_menu_item", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        menuItemRepository.save(menuItem);

        // Assert
        verify(statementSpec).param("name", menuItem.getName());
        verify(statementSpec).param("description", menuItem.getDescription());
        verify(statementSpec).param("price", menuItem.getPrice());
        verify(statementSpec).param("availableOnlyOnSite", menuItem.getAvailableOnlyOnSite());
        verify(statementSpec).param("imagePath", menuItem.getImagePath());
        verify(statementSpec).param("restaurantId", menuItem.getRestaurantId());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoMenuItemsExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(MenuItem.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(Arrays.asList());

        // Act
        var result = menuItemRepository.findAll(pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(mappedQuerySpec).list();
    }

    @Test
    void findByRestaurantId_ShouldReturnEmptyList_WhenNoMenuItemsExist() {
        // Arrange
        var restaurantId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(MenuItem.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(Arrays.asList());

        // Act
        var result = menuItemRepository.findByRestaurantId(restaurantId);

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("restaurantId", restaurantId);
        verify(mappedQuerySpec).list();
    }
}
