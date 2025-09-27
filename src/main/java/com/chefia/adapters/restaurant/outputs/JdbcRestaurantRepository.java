package com.chefia.adapters.restaurant.outputs;

import com.chefia.core.port.output.RestaurantRepositoryOutputPort;
import com.chefia.domain.model.BusinessHours;
import com.chefia.domain.model.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.Optional;

public class JdbcRestaurantRepository implements RestaurantRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcRestaurantRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Restaurant restaurantToInsert) {
        jdbcClient.sql("""
                        INSERT INTO restaurant
                            (name, active, created_at, restaurant_type, user_id, address_id)
                        VALUES
                            (:name, :active, :createdAt, :restaurantType, :userId, :addressId)
                        """)
                .param("name", restaurantToInsert.getName())
                .param("active", restaurantToInsert.isActive())
                .param("createdAt", restaurantToInsert.getCreatedAt())
                .param("restaurantType", restaurantToInsert.getRestaurantType().name())
                .param("userId", restaurantToInsert.getUser().getNrSeqUser())
                .param("addressId", restaurantToInsert.getAddress().getRestaurantId())
                .update();

        for (BusinessHours hours : restaurantToInsert.getBusinessHours()) {
            jdbcClient.sql("""
                            INSERT INTO business_hours
                                (week_day, opening_time, closing_time, restaurant_id)
                            VALUES
                                (:weekDay, :openingTime, :closingTime, LAST_INSERT_ID())
                            """)
                    .param("weekDay", hours.getWeekDay().name())
                    .param("openingTime", hours.getOpeningTime())
                    .param("closingTime", hours.getClosingTime())
                    .update();
        }
    }

    @Override
    public Optional<Restaurant> findById(Long restaurantId) {
        return jdbcClient.sql("""
                        SELECT * FROM restaurants
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .query(Restaurant.class)
                .optional();
    }

    @Override
    public List<Restaurant> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                         SELECT * FROM users LIMIT :size OFFSET :offset
                        """)
                .param("size", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(Restaurant.class)
                .list();
    }

    @Override
    public void update(Long restaurantId, Restaurant restaurantEntity) {
        jdbcClient.sql("""
                        UPDATE restaurants
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
                        DELETE FROM business_hours
                        WHERE restaurant_id = :id
                        """)
                .param("id", restaurantId)
                .update();

        jdbcClient.sql("""
                        DELETE FROM restaurant
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .update();
    }
}

