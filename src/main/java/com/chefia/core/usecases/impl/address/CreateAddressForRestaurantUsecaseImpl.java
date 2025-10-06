package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.AddressMapper;
import com.chefia.core.usecases.interfaces.address.CreateAddressForRestaurantUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class CreateAddressForRestaurantUsecaseImpl implements CreateAddressForRestaurantUsecase {

    private final AddressGateway addressGateway;
    private final RestaurantGateway restaurantGateway;
    private final AddressMapper addressMapper;

    @Override
    public AddressDTO execute(Long restaurantId, CreateAddressDTO createAddressDTO) {
        var restaurant = Optional.ofNullable(this.restaurantGateway
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId)));
        assert restaurant.isPresent();

        var addressToInsert = this.addressMapper.toCreateAddressEntityToRestaurant(restaurantId, createAddressDTO);

        var addressId = this.addressGateway.saveAddressForUser(addressToInsert);
        addressToInsert.setNrSeqAddress(addressId);

        return this.addressMapper.toAddressResponseDTO(addressToInsert);
    }
}
