package com.chefia.mapper;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.entities.Address;
import com.chefia.users.model.AddressDTO;
import com.chefia.users.model.CreateAddressDTO;
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

    public Address toCreateAddressEntity(com.chefia.addresses.model.CreateAddressDTO createAddressDTO) {
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

    public AddressDTO toResponseDTO(Address address) {
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
            addressDTOList.add(this.toResponseDTO(addressEntity));
        }
        return addressDTOList;
    }

    public Address toUpdateAddressEntity(Address address, UpdateAddressDTO updateAddressDTO) {
        return new Address(address.getNrSeqAddress(),
                updateAddressDTO.getStreet(),
                updateAddressDTO.getNumber(),
                updateAddressDTO.getCity(),
                updateAddressDTO.getState(),
                updateAddressDTO.getCountry(),
                address.getUser()
        );
    }
}
