package com.chefia.core.usecases.interfaces.restaurant;

import com.chefia.restaurants.model.RestaurantDTO;

public interface ReadRestauranteUsecase {

    RestaurantDTO execute(Long restaurantId);
}
