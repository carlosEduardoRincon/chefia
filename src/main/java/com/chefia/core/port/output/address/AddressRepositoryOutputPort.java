package com.chefia.core.port.output.address;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.domain.model.Address;

import java.util.Optional;

public interface AddressRepositoryOutputPort {

    long saveAddressForUser(Address addressToInsert);

    long saveAddressForRestaurant(Address addressToInsert);

    Optional<Address> findById(Long id);

    void updateAddress(Long id, UpdateAddressDTO updateAddressDTO);

    void deleteById(Long id);
}
