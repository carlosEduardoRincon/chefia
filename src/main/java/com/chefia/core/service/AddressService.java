package com.chefia.core.service;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.infra.mapper.AddressMapper;
import com.chefia.core.port.output.address.AddressRepository;
import com.chefia.core.port.output.user.UserRepository;
import com.chefia.infra.exception.AddressNotFoundException;
import com.chefia.infra.exception.UserNotFoundException;
import com.chefia.addresses.model.CreateAddressDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(
            UserRepository userRepository,
            AddressRepository addressRepository,
            AddressMapper addressMapper

    ) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public AddressDTO createAddressForUser(Long userId, CreateAddressDTO createAddressDTO) {
        var user = Optional.ofNullable(this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();
        
        var addressToInsert = this.addressMapper.toCreateAddressEntity(createAddressDTO);
        addressToInsert.setUser(user.get());
        user.get().getAddress().add(addressToInsert);
        
        this.userRepository.flush();
        return this.addressMapper.toAddressResponseDTO(addressToInsert);
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
        addressEntity.setState(updateAddressDTO.getState());
        addressEntity.setCountry(updateAddressDTO.getCountry());
        addressEntity.setNumber(updateAddressDTO.getNumber());

        addressRepository.flush();
        return addressMapper.toAddressResponseDTO(addressEntity);
    }

    public void deleteAddress(Long id) {
        this.addressRepository.deleteById(id);
    }
}
