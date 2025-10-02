package com.chefia.core.mapper;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toCreateAddressEntityDefault(CreateAddressDTO createAddressDTO) {
        return new Address(createAddressDTO.getStreet(),
                createAddressDTO.getNumber(),
                createAddressDTO.getCity(),
                createAddressDTO.getState(),
                createAddressDTO.getCountry()
        );
    }

    public Address toCreateAddressEntityToUser(Long userId, CreateAddressDTO createAddressDTO) {
        var address = toCreateAddressEntityDefault(createAddressDTO);
        address.setUserId(userId);
        return address;
    }

    public Address toCreateAddressEntityToRestaurant(Long restaurantId, CreateAddressDTO createAddressDTO) {
        var address = toCreateAddressEntityDefault(createAddressDTO);
        address.setRestaurantId(restaurantId);
        return address;
    }

    public AddressDTO toAddressResponseDTO(Address address) {
        var addressDTO = new AddressDTO();
        addressDTO.setId(address.getNrSeqAddress());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

}
