package com.chefia.core.entities;

import com.chefia.restaurants.model.CreateBusinessHoursDTO;

import java.time.LocalDateTime;

public class BusinessHours {
    private Long nrSeqBusinessHours;
    private CreateBusinessHoursDTO.WeekDayEnum weekDay;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private Long restaurantId;

    public BusinessHours() {
    }

    public BusinessHours(CreateBusinessHoursDTO.WeekDayEnum weekDay,
                         LocalDateTime openingTime,
                         LocalDateTime closingTime
    ) {
        this.weekDay = weekDay;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public CreateBusinessHoursDTO.WeekDayEnum getWeekDay() {
        return weekDay;
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }
}
