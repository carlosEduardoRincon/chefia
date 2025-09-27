package com.chefia.core.port.output;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.domain.model.Address;

import java.util.Optional;

public interface AddressRepositoryOutputPort {
    Optional<Address> findById(Long id);

    void saveAddress(Address addressToInsert);

    void updateAddress(Long id, UpdateAddressDTO updateAddressDTO);

    void deleteById(Long id);
}
