package com.chefia.core.usecases.interfaces.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;

public interface UpdateAddressUsecase {
    AddressDTO execute(Long addressId, UpdateAddressDTO body);
}
