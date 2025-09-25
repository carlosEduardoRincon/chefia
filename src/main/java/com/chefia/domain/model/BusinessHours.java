package com.chefia.domain.model;

import java.time.LocalTime;

public class BusinessHours {
    private Long nrSeqBusinessHours;
    private WeekDay weekDay;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Restaurant restaurant;
}
