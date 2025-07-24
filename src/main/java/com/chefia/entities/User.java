package com.chefia.entities;

import com.chefia.entities.enums.ProfileType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    @ManyToOne
    private List<Address> address;
    private boolean active;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;
    private ProfileType profileType;

    public User(String name,
                String email,
                String login,
                String password,
                List<Address> address,
                Boolean active,
                LocalDate createdAt,
                ProfileType profileType) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.address = address;
        this.active = active;
        this.createdAt = createdAt;
        this.profileType = profileType;
    }
}
