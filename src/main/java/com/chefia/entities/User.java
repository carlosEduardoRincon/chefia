package com.chefia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nr_seq_user")
    private Long nrSeqUser;
    private String name;
    private String email;
    private String login;
    private String password;
    private boolean active;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "profile_type")
    private String profileType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> address;

    public User(String name,
                String email,
                String login,
                String password,
                Boolean active,
                LocalDateTime createdAt,
                String profileType) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.active = active;
        this.createdAt = createdAt;
        this.profileType = profileType;
        this.address = new ArrayList<>();
    }
}
