package com.chefia.core.usecases.interfaces.address;

import com.chefia.addresses.model.AddressDTO;

public interface ReadAddressUsecase {
    AddressDTO execute(Long addressId);
}
