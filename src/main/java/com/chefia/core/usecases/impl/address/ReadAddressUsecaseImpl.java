package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.core.exceptions.AddressNotFoundException;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.mapper.AddressMapper;
import com.chefia.core.usecases.interfaces.address.ReadAddressUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ReadAddressUsecaseImpl implements ReadAddressUsecase {

    private final AddressGateway addressGateway;
    private final AddressMapper addressMapper;

    @Override
    public AddressDTO execute(Long addressId) {
        var address = Optional.ofNullable(this.addressGateway
                .findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId)));
        assert address.isPresent();
        return this.addressMapper.toAddressResponseDTO(address.get());
    }
}
