package com.chefia.core.port.input.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;

public interface AddressInputPort {
    AddressDTO createAddressForUser(Long userId, CreateAddressDTO createAddressDTO);

    AddressDTO findById(Long addressId);

    AddressDTO updateAddress(Long addressId, UpdateAddressDTO body);

    void deleteAddress(Long addressId);
}
