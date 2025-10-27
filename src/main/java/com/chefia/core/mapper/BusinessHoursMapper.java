package com.chefia.core.mapper;

import com.chefia.core.entities.BusinessHours;
import com.chefia.restaurants.model.CreateBusinessHoursDTO;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessHoursMapper {
    public List<BusinessHours> toEntity(List<CreateBusinessHoursDTO> businessHours) {
        var businessHoursEntityList = new ArrayList<BusinessHours>();
        for (CreateBusinessHoursDTO createBusinessHoursDTO : businessHours) {
            businessHoursEntityList.add(new BusinessHours(createBusinessHoursDTO.getWeekDay(),
                    createBusinessHoursDTO.getOpeningTime().toLocalDateTime(),
                    createBusinessHoursDTO.getClosingTime().toLocalDateTime())
            );
        }
        return businessHoursEntityList;
    }

    public List<CreateBusinessHoursDTO> toBusinessHoursResponseDTO(List<BusinessHours> businessHours) {
        var businessHoursDTOList = new ArrayList<CreateBusinessHoursDTO>();
        for (BusinessHours businessHoursItem : businessHours) {
            businessHoursDTOList.add(
                    new CreateBusinessHoursDTO()
                            .closingTime(businessHoursItem.getClosingTime().atOffset(ZoneOffset.ofHours(-3)))
                            .openingTime(businessHoursItem.getOpeningTime().atOffset(ZoneOffset.ofHours(-3)))
                            .weekDay(businessHoursItem.getWeekDay())
            );
        }
        return businessHoursDTOList;
    }
}
