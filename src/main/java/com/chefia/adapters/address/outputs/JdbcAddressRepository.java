package com.chefia.adapters.address.outputs;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.port.output.AddressRepositoryOutputPort;
import com.chefia.domain.model.Address;

import java.util.Optional;

public class JdbcAddressRepository implements AddressRepositoryOutputPort {
    @Override
    public Optional<Address> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void saveAddress(Address addressToInsert) {

    }

    @Override
    public void updateAddress(Long id, UpdateAddressDTO updateAddressDTO) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
