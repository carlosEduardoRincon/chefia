package com.chefia.core.mapper;

import com.chefia.core.entities.Restaurant;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantMapper {

    private final BusinessHoursMapper businessHoursMapper;

    public RestaurantMapper(BusinessHoursMapper businessHoursMapper) {
        this.businessHoursMapper = businessHoursMapper;
    }

    public Restaurant toEntity(CreateRestaurantDTO createRestaurantDTO) {
        return new Restaurant(createRestaurantDTO.getName(),
                true,
                LocalDateTime.now(),
                this.businessHoursMapper.toEntity(createRestaurantDTO.getBusinessHours()),
                RestaurantDTO.RestaurantTypeEnum.fromValue(createRestaurantDTO.getRestaurantType().name()),
                createRestaurantDTO.getUserId()
        );
    }

    public RestaurantDTO toRestaurantResponseDTO(Restaurant restaurant) {
        return new RestaurantDTO().id(restaurant.getNrSeqRestaurant())
                .name(restaurant.getName())
                .businessHours(restaurant.getBusinessHours() != null? this.businessHoursMapper.toBusinessHoursResponseDTO(restaurant.getBusinessHours()): null)
                .restaurantType(RestaurantDTO.RestaurantTypeEnum.fromValue(restaurant.getRestaurantType().name()))
                .userId(restaurant.getUserId())
                .active(restaurant.isActive())
                .createdAt(restaurant.getCreatedAt().atOffset(ZoneOffset.ofHours(-3)))
                .updatedAt(restaurant.getUpdatedAt() != null? restaurant.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3)) : null);
    }

    public List<RestaurantDTO> toResponseListDTO(List<Restaurant> restaurantList) {
        var restaurantsResponse = new ArrayList<RestaurantDTO>();
        for (var user : restaurantList) {
            restaurantsResponse.add(this.toRestaurantResponseDTO(user));
        }
        return restaurantsResponse;
    }
}
