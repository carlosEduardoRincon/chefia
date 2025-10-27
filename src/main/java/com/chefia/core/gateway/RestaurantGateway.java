package com.chefia.core.gateway;

import com.chefia.core.entities.Restaurant;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RestaurantGateway {

    long save(Restaurant restaurantToInsert);

    Optional<Restaurant> findById(Long restaurantId);

    List<Restaurant> findAll(Pageable pageable);

    void update(Long restaurantId, Restaurant restaurantEntity);

    void deleteById(Long restaurantId);
}
