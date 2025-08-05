package com.chefia.entities;

import com.chefia.validation.annotation.StrongPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nr_seq_user")
    private Long nrSeqUser;
    private String name;
    @Email
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String login;
    @StrongPassword
    private String password;
    private boolean active;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return login;
    }
}
