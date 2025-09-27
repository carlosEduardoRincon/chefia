package com.chefia.infra.mapper;

import com.chefia.addresses.model.CreateAddressDTO;
import com.chefia.domain.model.Address;
import com.chefia.users.model.AddressDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public com.chefia.addresses.model.AddressDTO toAddressResponseDTO(Address address) {
        var addressDTO = new com.chefia.addresses.model.AddressDTO();
        addressDTO.setId(address.getNrSeqAddress());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public AddressDTO toUserResponseDTO(Address address) {
        var addressDTO = new AddressDTO();
        addressDTO.setId(address.getNrSeqAddress());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public List<AddressDTO> toDTOList(List<Address> address) {
        var addressDTOList = new ArrayList<AddressDTO>();
        for (var addressEntity : address) {
            addressDTOList.add(this.toUserResponseDTO(addressEntity));
        }
        return addressDTOList;
    }
}
