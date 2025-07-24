package com.chefia.dtos;

import com.chefia.entities.Address;
import com.chefia.entities.enums.ProfileType;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDTO(String name,
                              String email,
                              String login,
                              List<Address> address,
                              boolean active,
                              LocalDate createdAt,
                              LocalDate updatedAt,
                              LocalDate deletedAt,
                              ProfileType profileType
) {
}
