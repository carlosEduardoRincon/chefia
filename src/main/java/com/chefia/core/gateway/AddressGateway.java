package com.chefia.core.gateway;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.entities.Address;

import java.util.Optional;

public interface AddressGateway {

    long saveAddressForUser(Address addressToInsert);

    long saveAddressForRestaurant(Address addressToInsert);

    Optional<Address> findById(Long id);

    void updateAddress(Long id, UpdateAddressDTO updateAddressDTO);

    void deleteById(Long id);
}
