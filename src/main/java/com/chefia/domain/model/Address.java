package com.chefia.domain.model;

public class Address {

    private Long nrSeqAddress;
    private String street;
    private Integer number;
    private String city;
    private String state;
    private String country;
    private Long userId;
    private Long restaurant;

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

    public void setUserId(Long userId) {
        this.userId = userId;
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
