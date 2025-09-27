package com.chefia.domain.model;

import java.time.LocalTime;

public class BusinessHours {
    private Long nrSeqBusinessHours;
    private WeekDay weekDay;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Long restaurantId;

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}
