package com.chefia.infra.mapper;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.domain.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public Address toCreateAddressEntity(CreateAddressDTO createAddressDTO) {
        return new Address(createAddressDTO.getStreet(),
                createAddressDTO.getNumber(),
                createAddressDTO.getCity(),
                createAddressDTO.getState(),
                createAddressDTO.getCountry()
        );
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
