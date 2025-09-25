package com.chefia.domain.model;

import java.math.BigDecimal;

public class MenuItem {
    private Long nrSeqMenuItem;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean availableOnlyOnSite;
    private String imagePath;
    private Long restaurantId;
}
