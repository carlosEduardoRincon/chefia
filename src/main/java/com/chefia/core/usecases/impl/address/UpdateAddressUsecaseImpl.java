package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.exceptions.AddressNotFoundException;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.mapper.AddressMapper;
import com.chefia.core.usecases.interfaces.address.UpdateAddressUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateAddressUsecaseImpl implements UpdateAddressUsecase {

    private final AddressGateway addressGateway;
    private final AddressMapper addressMapper;

    @Override
    public AddressDTO execute(Long addressId, UpdateAddressDTO updateAddressDTO) {
        var addressEntity = this.addressGateway
                .findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));

        addressEntity.setStreet(updateAddressDTO.getStreet());
        addressEntity.setCity(updateAddressDTO.getCity());
        addressEntity.setState(updateAddressDTO.getState());
        addressEntity.setCountry(updateAddressDTO.getCountry());
        addressEntity.setNumber(updateAddressDTO.getNumber());

        this.addressGateway.updateAddress(addressId, updateAddressDTO);
        return addressMapper.toAddressResponseDTO(addressEntity);
    }
}
