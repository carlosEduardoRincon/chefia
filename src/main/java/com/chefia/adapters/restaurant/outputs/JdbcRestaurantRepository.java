package com.chefia.adapters.restaurant.outputs;

import com.chefia.core.port.output.restaurant.RestaurantRepositoryOutputPort;
import com.chefia.domain.model.BusinessHours;
import com.chefia.domain.model.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public class JdbcRestaurantRepository implements RestaurantRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcRestaurantRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long save(Restaurant restaurantToInsert) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                        INSERT INTO chefia.restaurants
                            (name, active, created_at, restaurant_type, user_id)
                        VALUES
                            (:name, :active, :createdAt, :restaurantType, :userId)
                        """)
                .param("name", restaurantToInsert.getName())
                .param("active", restaurantToInsert.isActive())
                .param("createdAt", restaurantToInsert.getCreatedAt())
                .param("restaurantType", restaurantToInsert.getRestaurantType().name())
                .param("userId", restaurantToInsert.getUserId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;
        var restaurantId = keys.get("nr_seq_restaurant");

        return restaurantId != null? ((Number) restaurantId).longValue() : null;
    }

    @Override
    public Optional<Restaurant> findById(Long restaurantId) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.restaurants
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .query(Restaurant.class)
                .optional();
    }

    @Override
    public List<Restaurant> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                         SELECT * FROM chefia.restaurants LIMIT :size OFFSET :offset
                        """)
                .param("size", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(Restaurant.class)
                .list();
    }

    @Override
    public void update(Long restaurantId, Restaurant restaurantEntity) {
        jdbcClient.sql("""
                        UPDATE chefia.restaurants
                        SET name = :name,
                            active = :active,
                            restaurant_type = :restaurantType
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .param("name", restaurantEntity.getName())
                .param("active", restaurantEntity.isActive())
                .param("restaurantType", restaurantEntity.getRestaurantType().name())
                .update();
    }

    @Override
    public void deleteById(Long restaurantId) {
        jdbcClient.sql("""
                        DELETE FROM chefia.restaurants
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .update();
    }
}

