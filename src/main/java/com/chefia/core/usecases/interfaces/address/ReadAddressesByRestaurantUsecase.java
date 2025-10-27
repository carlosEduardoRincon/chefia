package com.chefia.core.usecases.interfaces.address;

import com.chefia.addresses.model.AddressDTO;

import java.util.List;

public interface ReadAddressesByRestaurantUsecase {
    List<AddressDTO> execute(Long restaurantId);
}
