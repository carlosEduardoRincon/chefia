package com.chefia.core.port.output;

import com.chefia.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantRepositoryOutputPort {

    void save(Restaurant restaurantToInsert);

    Optional<Restaurant> findById(Long restaurantId);

    Page<Restaurant> findAll(Pageable pageable);

    void update(Long restaurantId, Restaurant restaurantEntity);

    void deleteById(Long restaurantId);
}
