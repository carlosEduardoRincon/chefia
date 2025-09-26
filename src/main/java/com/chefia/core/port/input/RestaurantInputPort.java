package com.chefia.core.port.input;

import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;

public interface RestaurantInputPort {

    RestaurantDTO saveRestaurant(CreateRestaurantDTO body);

    RestaurantDTO findById(Long restaurantId);

    PaginatedRestaurantsDTO findAll(Integer page, Integer perPage);

    RestaurantDTO updateRestaurant(Long restaurantId, UpdateRestaurantDTO body);

    void deleteRestaurant(Long restaurantId);
}
