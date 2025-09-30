package com.chefia.core.port.output.businesshour;

import com.chefia.domain.model.BusinessHours;

import java.util.List;

public interface BusinessHourRepositoryOutputPort {
    void save(List<BusinessHours> businessHoursList, Long userRestaurantId);

    List<BusinessHours> findById(Long restaurantId);

    void deleteByRestaurantIdBusinessHours(Long restaurantId);
}
