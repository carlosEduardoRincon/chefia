package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.AddressMapper;
import com.chefia.core.usecases.interfaces.address.CreateAddressForUserUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class CreateAddressForUserUsecaseImpl implements CreateAddressForUserUsecase {

    private final AddressGateway addressGateway;
    private final UserGateway userGateway;
    private final AddressMapper addressMapper;

    @Override
    public AddressDTO execute(Long userId, CreateAddressDTO createAddressDTO) {
        var user = Optional.ofNullable(this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();

        var addressToInsert = this.addressMapper.toCreateAddressEntityToUser(userId, createAddressDTO);

        var addressId = this.addressGateway.saveAddressForUser(addressToInsert);
        addressToInsert.setNrSeqAddress(addressId);

        return this.addressMapper.toAddressResponseDTO(addressToInsert);
    }
}
