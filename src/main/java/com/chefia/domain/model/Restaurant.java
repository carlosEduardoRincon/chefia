package com.chefia.domain.model;

import com.chefia.restaurants.model.RestaurantDTO;

import java.time.LocalDateTime;
import java.util.List;

public class Restaurant {

    private Long nrSeqRestaurant;
    private String name;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BusinessHours> businessHours;
    private RestaurantDTO.RestaurantTypeEnum restaurantType;
    private Long userId;

    public Restaurant() {
    }

    public Restaurant(String name,
                      boolean active,
                      LocalDateTime createdAt,
                      List<BusinessHours> businessHours,
                      RestaurantDTO.RestaurantTypeEnum restaurantType,
                      Long userId
    ) {
        this.name = name;
        this.active = active;
        this.createdAt = createdAt;
        this.businessHours = businessHours;
        this.restaurantType = restaurantType;
        this.userId = userId;
    }

    public Long getNrSeqRestaurant() {
        return nrSeqRestaurant;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<BusinessHours> getBusinessHours() {
        return businessHours;
    }

    public RestaurantDTO.RestaurantTypeEnum getRestaurantType() {
        return restaurantType;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setNrSeqRestaurant(Long nrSeqRestaurant) {
        this.nrSeqRestaurant = nrSeqRestaurant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBusinessHours(List<BusinessHours> businessHours) {
        this.businessHours = businessHours;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRestaurantType(RestaurantDTO.RestaurantTypeEnum restaurantType) {
        this.restaurantType = restaurantType;
    }
}
