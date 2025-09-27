package com.chefia.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Restaurant {

    private Long nrSeqRestaurant;
    private String name;
    private boolean active;
    private LocalDateTime createdAt;
    private List<BusinessHours> businessHours;
    private RestaurantType restaurantType;
    private User user;
    private Address address;

    public Restaurant() {
    }

    public Restaurant(Long nrSeqRestaurant, String name, boolean active, LocalDateTime createdAt, List<BusinessHours> businessHours, User user, Address address) {
        this.nrSeqRestaurant = nrSeqRestaurant;
        this.name = name;
        this.active = active;
        this.createdAt = createdAt;
        this.businessHours = businessHours;
        this.user = user;
        this.address = address;
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

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public User getUser() {
        return user;
    }

    public Address getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBusinessHours(List<BusinessHours> businessHours) {
        this.businessHours = businessHours;
    }
}
