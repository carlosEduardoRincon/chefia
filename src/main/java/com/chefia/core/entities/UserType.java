package com.chefia.core.entities;

import java.time.LocalDateTime;

public class UserType {

    private Long nrSeqUserType;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserType(String name, String description, boolean active, LocalDateTime createdAt) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getNrSeqUserType() {
        return nrSeqUserType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setNrSeqUserType(Long nrSeqUserType) {
        this.nrSeqUserType = nrSeqUserType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "nrSeqUserType=" + nrSeqUserType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
