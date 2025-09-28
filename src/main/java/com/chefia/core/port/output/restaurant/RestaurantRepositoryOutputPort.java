package com.chefia.core.port.output.restaurant;

import com.chefia.domain.model.Restaurant;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryOutputPort {

    void save(Restaurant restaurantToInsert);

    Optional<Restaurant> findById(Long restaurantId);

    List<Restaurant> findAll(Pageable pageable);

    void update(Long restaurantId, Restaurant restaurantEntity);

    void deleteById(Long restaurantId);
}
