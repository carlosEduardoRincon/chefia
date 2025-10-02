package com.chefia.core.gateway;

import com.chefia.core.entities.BusinessHours;

import java.util.List;

public interface BusinessHourGateway {
    void save(List<BusinessHours> businessHoursList, Long userRestaurantId);

    List<BusinessHours> findById(Long restaurantId);

    void deleteByRestaurantIdBusinessHours(Long restaurantId);
}
