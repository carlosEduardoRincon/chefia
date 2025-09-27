package com.chefia.adapters.restaurant.outputs;

import com.chefia.core.port.output.RestaurantRepositoryOutputPort;
import com.chefia.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JdbcRestaurantRepository implements RestaurantRepositoryOutputPort {
    @Override
    public void save(Restaurant restaurantToInsert) {

    }

    @Override
    public Optional<Restaurant> findById(Long restaurantId) {
        return Optional.empty();
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long restaurantId, Restaurant restaurantEntity) {

    }

    @Override
    public void deleteById(Long restaurantId) {

    }
}
