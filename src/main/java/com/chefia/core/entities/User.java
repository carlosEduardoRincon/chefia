package com.chefia.core.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {

    private Long nrSeqUser;
    private String name;
    private String email;
    private String login;
    private String password;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userTypeId;
    private List<Address> address;

    public User() {
    }

    public User(String name,
                String email,
                String login,
                String password,
                Boolean active,
                LocalDateTime createdAt,
                Long userTypeId) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.active = active;
        this.createdAt = createdAt;
        this.userTypeId = userTypeId;
        this.address = new ArrayList<>();
    }

    public Long getNrSeqUser() {
        return nrSeqUser;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setNrSeqUser(Long nrSeqUser) {
        this.nrSeqUser = nrSeqUser;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userTypeId));
    }

    @Override
    public String getUsername() {
        return login;
    }
}
