package com.chefia.domain.model;

import com.chefia.infra.validation.annotation.StrongPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class User implements UserDetails {

    private Long nrSeqUser;
    private String name;
    private String email;
    private String login;
    private String password;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserType userType;
    private List<Address> address;

    public User(String name,
                String email,
                String login,
                String password,
                Boolean active,
                LocalDateTime createdAt,
                UserType userType) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.active = active;
        this.createdAt = createdAt;
        this.userType = userType;
        this.address = new ArrayList<>();
    }

    public void setActive(Boolean status) {
        this.active = status;
    }

    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return login;
    }
}
