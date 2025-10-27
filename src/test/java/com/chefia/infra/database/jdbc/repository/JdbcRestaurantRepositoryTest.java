package com.chefia.infra.database.jdbc.repository;

import com.chefia.core.entities.Restaurant;
import com.chefia.restaurants.model.RestaurantDTO;
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
class JdbcRestaurantRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<Restaurant> mappedQuerySpec;

    @InjectMocks
    private JdbcRestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setNrSeqRestaurant(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setActive(true);
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        restaurant.setUserId(1L);
    }

    @Test
    void save_ShouldReturnRestaurantId_WhenRestaurantIsSaved() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_restaurant", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        var result = restaurantRepository.save(restaurant);

        // Assert
        assertEquals(1L, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec, times(5)).param(anyString(), any());
        verify(statementSpec).update(any(GeneratedKeyHolder.class));
    }

    @Test
    void findById_ShouldReturnRestaurant_WhenRestaurantExists() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Restaurant.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.of(restaurant));

        // Act
        var result = restaurantRepository.findById(restaurantId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(restaurant, result.get());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findById_ShouldReturnEmpty_WhenRestaurantDoesNotExist() {
        // Arrange
        var restaurantId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Restaurant.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.optional()).thenReturn(Optional.empty());

        // Act
        var result = restaurantRepository.findById(restaurantId);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(mappedQuerySpec).optional();
    }

    @Test
    void findAll_ShouldReturnRestaurantList_WhenRestaurantsExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        var restaurantList = Arrays.asList(restaurant, new Restaurant());
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Restaurant.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(restaurantList);

        // Act
        var result = restaurantRepository.findAll(pageable);

        // Assert
        assertEquals(2, result.size());
        assertEquals(restaurantList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("size", pageable.getPageSize());
        verify(statementSpec).param("offset", pageable.getOffset());
        verify(mappedQuerySpec).list();
    }

    @Test
    void update_ShouldCallUpdateWithCorrectParameters_WhenRestaurantIsUpdated() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        restaurantRepository.update(restaurantId, restaurant);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(statementSpec).param("name", restaurant.getName());
        verify(statementSpec).param("active", restaurant.isActive());
        verify(statementSpec).param("restaurantType", restaurant.getRestaurantType().name());
        verify(statementSpec).update();
    }

    @Test
    void deleteById_ShouldCallDeleteWithCorrectParameter_WhenRestaurantIsDeleted() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        restaurantRepository.deleteById(restaurantId);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(statementSpec).update();
    }

    @Test
    void save_ShouldCallCorrectParameters_WhenSavingRestaurant() {
        // Arrange
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(0);
            keyHolder.getKeyList().clear();
            keyHolder.getKeyList().add(Map.of("nr_seq_restaurant", 1L));
            return 1;
        }).when(statementSpec).update(any(GeneratedKeyHolder.class));

        // Act
        restaurantRepository.save(restaurant);

        // Assert
        verify(statementSpec).param("name", restaurant.getName());
        verify(statementSpec).param("active", restaurant.isActive());
        verify(statementSpec).param("createdAt", restaurant.getCreatedAt());
        verify(statementSpec).param("restaurantType", restaurant.getRestaurantType().name());
        verify(statementSpec).param("userId", restaurant.getUserId());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoRestaurantsExist() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(Restaurant.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(Arrays.asList());

        // Act
        var result = restaurantRepository.findAll(pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(mappedQuerySpec).list();
    }

    @Test
    void update_ShouldHandleDifferentRestaurantTypes_WhenUpdating() {
        // Arrange
        var restaurantId = 1L;
        restaurant.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.FAST_FOOD);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        restaurantRepository.update(restaurantId, restaurant);

        // Assert
        verify(statementSpec).param("restaurantType", "FAST_FOOD");
    }
}
