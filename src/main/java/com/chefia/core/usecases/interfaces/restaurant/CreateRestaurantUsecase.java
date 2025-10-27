package com.chefia.core.usecases.interfaces.restaurant;

import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.RestaurantDTO;

public interface CreateRestaurantUsecase {
    RestaurantDTO execute(CreateRestaurantDTO body);
}
