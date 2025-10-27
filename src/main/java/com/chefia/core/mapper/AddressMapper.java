package com.chefia.core.mapper;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.core.entities.Address;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {

    public Address toEntity(CreateAddressDTO createAddressDTO) {
        return new Address(
                createAddressDTO.getStreet(),
                createAddressDTO.getNumber(),
                createAddressDTO.getCity(),
                createAddressDTO.getState(),
                createAddressDTO.getCountry()
        );
    }

    public Address toUserAddressEntity(Long userId, CreateAddressDTO createAddressDTO) {
        var address = toEntity(createAddressDTO);
        address.setUserId(userId);
        return address;
    }

    public Address toRestaurantAddressEntity(Long restaurantId, CreateAddressDTO createAddressDTO) {
        var address = toEntity(createAddressDTO);
        address.setRestaurantId(restaurantId);
        return address;
    }

    public AddressDTO toAddressResponseDTO(Address address) {
        return new AddressDTO()
                .id(address.getNrSeqAddress())
                .street(address.getStreet())
                .number(address.getNumber())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry());
    }

    public List<AddressDTO> toResponseListDTO(List<Address> addressList) {
        return addressList.stream()
                .map(this::toAddressResponseDTO)
                .toList();
    }
}
