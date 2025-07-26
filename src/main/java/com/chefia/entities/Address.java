package com.chefia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nr_seq_address")
    private Long nrSeqAddress;
    private String street;
    private Integer number;
    private String city;
    private String state;
    private String country;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nr_seq_user", nullable = false)
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
