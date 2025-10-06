package com.chefia.core.controllers;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.usecases.interfaces.address.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class AddressController {

    private final CreateAddressForUserUsecase createAddressForUserUsecase;
    private final CreateAddressForRestaurantUsecase createAddressForRestaurantUsecase;
    private final ReadAddressUsecase readAddressUsecase;
    private final UpdateAddressUsecase updateAddressUsecase;
    private final DeleteAddressUsecase deleteAddressUsecase;
    
    public AddressDTO createAddressForUser(Long userId, CreateAddressDTO createAddressDTO)
    {
        return this.createAddressForUserUsecase.execute(userId, createAddressDTO);
    }

    public AddressDTO createAddressForRestaurant(Long restaurantId, CreateAddressDTO createAddressDTO)
    {
        return this.createAddressForRestaurantUsecase.execute(restaurantId, createAddressDTO);
    }

    public AddressDTO findById(Long addressId)
    {
        return this.readAddressUsecase.execute(addressId);
    }

    public AddressDTO updateAddress(Long addressId, UpdateAddressDTO body)
    {
        return this.updateAddressUsecase.execute(addressId, body);
    }

    public void deleteAddress(Long addressId)
    {
        this.deleteAddressUsecase.execute(addressId);
    }
}
