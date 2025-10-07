package com.chefia.core.gateway;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.entities.Address;

import java.util.List;
import java.util.Optional;

public interface AddressGateway {

    long saveUserAddress(Address addressToInsert);

    long saveRestaurantAddress(Address addressToInsert);

    Optional<Address> findById(Long addressId);

    List<Address> findByUserId(Long userId);

    List<Address> findByRestaurantId(Long restaurantId);

    void updateAddress(Long addressId, UpdateAddressDTO updateAddressDTO);

    void deleteById(Long addressId);
}
