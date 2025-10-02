package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.usecases.interfaces.address.AddressInputPort;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.mapper.AddressMapper;
import com.chefia.core.exceptions.AddressNotFoundException;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.addresses.model.CreateAddressDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService implements AddressInputPort {

    private final AddressGateway addressGateway;
    private final UserGateway userGateway;
    private final RestaurantGateway restaurantGateway;
    private final AddressMapper addressMapper;

    public AddressService(AddressGateway addressGateway,
                          UserGateway userGateway,
                          RestaurantGateway restaurantGateway,
                          AddressMapper addressMapper
    ) {
        this.addressGateway = addressGateway;
        this.userGateway = userGateway;
        this.restaurantGateway = restaurantGateway;
        this.addressMapper = addressMapper;
    }

    public AddressDTO createAddressForUser(Long userId, CreateAddressDTO createAddressDTO) {
        var user = Optional.ofNullable(this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();

        var addressToInsert = this.addressMapper.toCreateAddressEntityToUser(userId, createAddressDTO);

        var addressId = this.addressGateway.saveAddressForUser(addressToInsert);
        addressToInsert.setNrSeqAddress(addressId);

        return this.addressMapper.toAddressResponseDTO(addressToInsert);
    }

    public AddressDTO createAddressForRestaurant(Long restaurantId, CreateAddressDTO createAddressDTO) {
        var restaurant = Optional.ofNullable(this.restaurantGateway
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId)));
        assert restaurant.isPresent();

        var addressToInsert = this.addressMapper.toCreateAddressEntityToRestaurant(restaurantId, createAddressDTO);

        var addressId = this.addressGateway.saveAddressForUser(addressToInsert);
        addressToInsert.setNrSeqAddress(addressId);

        return this.addressMapper.toAddressResponseDTO(addressToInsert);
    }

    public AddressDTO findById(Long id) {
        var address = Optional.ofNullable(this.addressGateway
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id)));
        assert address.isPresent();
        return this.addressMapper.toAddressResponseDTO(address.get());
    }

    public AddressDTO updateAddress(Long id, UpdateAddressDTO updateAddressDTO) {
        var addressEntity = this.addressGateway
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id));

        addressEntity.setStreet(updateAddressDTO.getStreet());
        addressEntity.setCity(updateAddressDTO.getCity());
        addressEntity.setState(updateAddressDTO.getState());
        addressEntity.setCountry(updateAddressDTO.getCountry());
        addressEntity.setNumber(updateAddressDTO.getNumber());

        this.addressGateway.updateAddress(id, updateAddressDTO);
        return addressMapper.toAddressResponseDTO(addressEntity);
    }

    public void deleteAddress(Long id) {
        this.addressGateway.deleteById(id);
    }
}
