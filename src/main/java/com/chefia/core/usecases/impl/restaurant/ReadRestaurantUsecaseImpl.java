package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.core.usecases.interfaces.restaurant.ReadRestauranteUsecase;
import com.chefia.restaurants.model.RestaurantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ReadRestaurantUsecaseImpl implements ReadRestauranteUsecase {

    private final RestaurantGateway restaurantGateway;
    private final BusinessHourGateway businessHourGateway;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantDTO execute(Long restaurantId) {
        var restaurant = Optional.ofNullable(this.restaurantGateway
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant Item not found with id: " + restaurantId)));
        assert restaurant.isPresent();

        var businessHours = this.businessHourGateway.findById(restaurantId);
        restaurant.get().setBusinessHours(businessHours);
        return this.restaurantMapper.toRestaurantResponseDTO(restaurant.get());
    }
}
