package com.chefia.infra.mapper;

import com.chefia.domain.model.Restaurant;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantMapper {
    public Restaurant toEntity(CreateRestaurantDTO createRestaurantDTO) {
        return new Restaurant();
    }

    public RestaurantDTO toRestaurantResponseDTO(Restaurant restaurantToInsert) {
        return new RestaurantDTO();
    }

    public List<RestaurantDTO> toResponseListDTO(List<Restaurant> content) {
        return new ArrayList<>();
    }
}
