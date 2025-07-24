package com.chefia.mapper;

import com.chefia.entities.Address;
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

    public List<Address> toEntityList(List<AddressDTO> addressDTOList) {
        var addressEntityList = new ArrayList<Address>();
        for (var addressDTO : addressDTOList) {
            addressEntityList.add(this.toEntity(addressDTO));
        }
        return addressEntityList;
    }
}
