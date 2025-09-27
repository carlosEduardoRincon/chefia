package com.chefia.domain.model;

import java.math.BigDecimal;

public class MenuItem {
    private Long nrSeqMenuItem;
    private String name;
    private String description;
    private Double price;
    private Boolean availableOnlyOnSite;
    private String imagePath;
    private Long restaurantId;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAvailableOnlyOnSite(Boolean availableOnlyOnSite) {
        this.availableOnlyOnSite = availableOnlyOnSite;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
