package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.BusinessHoursMapper;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.core.usecases.interfaces.restaurant.CreateRestaurantUsecase;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CreateRestaurantUsecaseImpl implements CreateRestaurantUsecase {

    private final RestaurantGateway restaurantGateway;
    private final BusinessHourGateway businessHourGateway;
    private final RestaurantMapper restaurantMapper;
    private final BusinessHoursMapper businessHoursMapper;

    @Override
    public RestaurantDTO execute(CreateRestaurantDTO createRestaurantDTO) {
        var restaurantToInsert = this.restaurantMapper.toEntity(createRestaurantDTO);

        var restaurantId = this.restaurantGateway.save(restaurantToInsert);
        restaurantToInsert.setNrSeqRestaurant(restaurantId);

        var newBusinessHourslist = this.businessHoursMapper.toEntity(createRestaurantDTO.getBusinessHours());
        this.businessHourGateway.save(newBusinessHourslist, restaurantId);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantToInsert);
    }
}
