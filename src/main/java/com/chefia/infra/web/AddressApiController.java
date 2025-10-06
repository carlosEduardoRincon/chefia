package com.chefia.infra.web;

import com.chefia.addresses.api.AddressApi;
import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.controllers.AddressController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
public class AddressApiController implements AddressApi {

    private final AddressController addressController;

    @Override
    public ResponseEntity<AddressDTO> createAddressForUser(Long userId, CreateAddressDTO createAddressDTO)
    {
        log.info("[POST] - Create Address for User");
        var createdAddress = this.addressController.createAddressForUser(userId, createAddressDTO);
        return ResponseEntity.status(201).body(createdAddress);
    }

    @Override
    public ResponseEntity<AddressDTO> createAddressForRestaurant(Long restaurantId, CreateAddressDTO createAddressDTO)
    {
        log.info("[POST] - Create Address for Restaurant");
        var createdAddress = this.addressController.createAddressForRestaurant(restaurantId, createAddressDTO);
        return ResponseEntity.status(201).body(createdAddress);
    }

    @Override
    public ResponseEntity<AddressDTO> getAddress(Long addressId)
    {
        log.info("[GET] - List Address");
        var getAddress = this.addressController.findById(addressId);
        return ResponseEntity.ok(getAddress);
    }

    @Override
    public ResponseEntity<AddressDTO> updateAddress(Long addressId, UpdateAddressDTO body)
    {
        log.info("[PUT] - Update Address");
        var updatedAddress = this.addressController.updateAddress(addressId, body);
        return ResponseEntity.ok().body(updatedAddress);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(Long addressId)
    {
        log.info("[DELETE] - Remove Address");
        this.addressController.deleteAddress(addressId);
        return ResponseEntity.ok().build();
    }
}
