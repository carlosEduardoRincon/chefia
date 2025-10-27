package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.BusinessHoursMapper;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.core.usecases.interfaces.restaurant.UpdateRestaurantUsecase;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateRestaurantUsecaseImpl implements UpdateRestaurantUsecase {

    private final RestaurantGateway restaurantGateway;
    private final BusinessHourGateway businessHourGateway;
    private final RestaurantMapper restaurantMapper;
    private final BusinessHoursMapper businessHoursMapper;

    @Override
    public RestaurantDTO execute(Long restaurantId, UpdateRestaurantDTO updateRestaurantDTO) {
        var restaurantEntity = this.restaurantGateway
                .findById(restaurantId)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurantEntity.setName(updateRestaurantDTO.getName());
        restaurantEntity.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.fromValue(updateRestaurantDTO.getRestaurantType().name()));
        restaurantEntity.setActive(updateRestaurantDTO.isActive());
        restaurantEntity.setBusinessHours(this.businessHoursMapper.toEntity(updateRestaurantDTO.getBusinessHours()));

        if (!updateRestaurantDTO.getBusinessHours().isEmpty()) {
            this.businessHourGateway.deleteByRestaurantIdBusinessHours(restaurantId);
            var newBusinessHourslist = this.businessHoursMapper.toEntity(updateRestaurantDTO.getBusinessHours());
            this.businessHourGateway.save(newBusinessHourslist, restaurantId);
        }

        this.restaurantGateway.update(restaurantId, restaurantEntity);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantEntity);
    }
}
