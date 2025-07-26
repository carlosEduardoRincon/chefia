package com.chefia.services;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.mapper.AddressMapper;
import com.chefia.repositories.AddressRepository;
import com.chefia.services.exceptions.AddressNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(
            AddressRepository addressRepository,
            AddressMapper addressMapper

    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public AddressDTO findById(Long id) {
        var address = Optional.ofNullable(this.addressRepository
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id)));
        assert address.isPresent();
        return this.addressMapper.toAddressResponseDTO(address.get());
    }

    public AddressDTO updateAddress(Long id, UpdateAddressDTO updateAddressDTO) {
        var addressEntity = this.addressRepository
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id));

        addressEntity.setStreet(updateAddressDTO.getStreet());
        addressEntity.setCity(updateAddressDTO.getCity());
        addressEntity.setNumber(updateAddressDTO.getNumber());
        addressEntity.setState(updateAddressDTO.getState());
        addressEntity.setCountry(updateAddressDTO.getCountry());

        addressRepository.flush();
        return addressMapper.toAddressResponseDTO(addressEntity);
    }

    public void deleteAddress(Long id) {
        this.addressRepository.deleteById(id);
    }
}
