package com.chefia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Address {
    @Id
    private Long id;
    private String street;
    private Integer number;
    private String city;
    private String state;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
}
