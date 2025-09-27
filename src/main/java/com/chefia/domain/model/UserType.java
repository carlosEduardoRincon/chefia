package com.chefia.domain.model;

import java.time.LocalDateTime;
import java.util.Date;

public class UserType {

    private Long nrSeqUserType;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
