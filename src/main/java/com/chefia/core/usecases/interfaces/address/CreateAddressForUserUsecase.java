package com.chefia.core.usecases.interfaces.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;

public interface CreateAddressForUserUsecase {
    AddressDTO execute(Long userId, CreateAddressDTO createAddressDTO);
}
