package com.chefia.core.usecases.interfaces.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;

public interface CreateAddressForRestaurantUsecase {
    AddressDTO execute(Long restaurantId, CreateAddressDTO createAddressDTO);
}
