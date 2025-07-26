package com.chefia.controllers;

import com.chefia.addresses.api.AddressApi;
import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.services.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AddressController implements AddressApi {
    
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public ResponseEntity<AddressDTO> getAddress(Long addressId)
    {
        log.info("[GET] - List Address");
        var getAddress = this.addressService.findById(addressId);
        return ResponseEntity.ok(getAddress);
    }

    @Override
    public ResponseEntity<AddressDTO> updateAddress(Long addressId, UpdateAddressDTO body)
    {
        log.info("[PUT] - Update Address");
        var updatedAddress = this.addressService.updateAddress(addressId, body);
        var status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).body(updatedAddress);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(Long addressId)
    {
        log.info("[DELETE] - Remove Address");
        this.addressService.deleteAddress(addressId);
        return ResponseEntity.ok().build();
    }
}
