package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.usecases.interfaces.restaurant.DeleteRestaurantUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeleteRestaurantUsecaseImpl implements DeleteRestaurantUsecase {

    private final RestaurantGateway restaurantGateway;
    private final BusinessHourGateway businessHourGateway;

    @Override
    public void execute(Long restaurantId) {
        this.businessHourGateway.deleteByRestaurantIdBusinessHours(restaurantId);
        this.restaurantGateway.deleteById(restaurantId);
    }
}
