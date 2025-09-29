package com.chefia.domain.model;

public class Address {

    private Long nrSeqAddress;
    private String street;
    private Integer number;
    private String city;
    private String state;
    private String country;
    private Long userId;
    private Long restaurantId;

    public Address(String street,
                   Integer number,
                   String city,
                   String state,
                   String country) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Long getNrSeqAddress() {
        return nrSeqAddress;
    }

    public String getStreet() {
        return street;
    }

    public Integer getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setNrSeqAddress(Long nrSeqAddress) {
        this.nrSeqAddress = nrSeqAddress;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
