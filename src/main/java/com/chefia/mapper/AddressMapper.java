package com.chefia.mapper;

import com.chefia.entities.Address;
import com.chefia.entities.User;
import com.chefia.users.model.AddressDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper {
    public Address toEntity(AddressDTO addressDTO) {
        return new Address(addressDTO.getStreet(),
                addressDTO.getNumber(),
                addressDTO.getCity(),
                addressDTO.getState(),
                addressDTO.getCountry()
        );
    }

    public List<Address> toEntityList(List<AddressDTO> addressDTOList, User userEntity) {
        var addressEntityList = new ArrayList<Address>();
        for (var addressDTO : addressDTOList) {
            var addressEntity = toEntity(addressDTO);
            addressEntity.setUser(userEntity);
            addressEntityList.add(addressEntity);
        }
        return addressEntityList;
    }

    public com.chefia.addresses.model.AddressDTO toAddressResponseDTO(Address address) {
        var addressDTO = new com.chefia.addresses.model.AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public AddressDTO toResponseDTO(Address address) {
        var addressDTO = new AddressDTO();
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
            addressDTOList.add(this.toResponseDTO(addressEntity));
        }
        return addressDTOList;
    }
}
