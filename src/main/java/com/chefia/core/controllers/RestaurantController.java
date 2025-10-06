package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.restaurant.*;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Component
public class RestaurantController {

    private final CreateRestaurantUsecase createRestaurantUsecase;
    private final ReadAllRestauranteUsecase readAllRestauranteUsecase;
    private final ReadRestauranteUsecase readRestauranteUsecase;
    private final UpdateRestaurantUsecase updateRestaurantUsecase;
    private final DeleteRestaurantUsecase deleteRestaurantUsecase;

    public RestaurantDTO createRestaurant(CreateRestaurantDTO createRestaurantDTO)
    {
        return this.createRestaurantUsecase.execute(createRestaurantDTO);
    }

    public RestaurantDTO getRestaurant(Long restaurantId)
    {
        return this.readRestauranteUsecase.execute(restaurantId);
    }

    public PaginatedRestaurantsDTO listRestaurants(Integer page, Integer perPage)
    {
        return this.readAllRestauranteUsecase.execute(page, perPage);
    }

    public RestaurantDTO updateRestaurant(Long restaurantId, UpdateRestaurantDTO updateRestaurantDTO)
    {
        return this.updateRestaurantUsecase.execute(restaurantId, updateRestaurantDTO);
    }

    public void deleteRestaurant(Long restaurantId)
    {
        this.deleteRestaurantUsecase.execute(restaurantId);
    }
}
