package com.chefia.core.usecases.interfaces.restaurant;

import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;

public interface UpdateRestaurantUsecase {
    RestaurantDTO execute(Long restaurantId, UpdateRestaurantDTO body);
}
