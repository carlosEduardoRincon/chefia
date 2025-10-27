package com.chefia.core.usecases.interfaces.restaurant;

import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;

public interface ReadAllRestauranteUsecase {

    PaginatedRestaurantsDTO execute(Integer page, Integer perPage);
}
