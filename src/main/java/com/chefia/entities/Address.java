package com.chefia.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Address {
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
}
