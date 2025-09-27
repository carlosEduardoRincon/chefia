package com.chefia.core.service;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.port.input.AddressInputPort;
import com.chefia.core.port.output.AddressRepositoryOutputPort;
import com.chefia.core.port.output.UserRepositoryOutputPort;
import com.chefia.infra.mapper.AddressMapper;
import com.chefia.infra.exception.AddressNotFoundException;
import com.chefia.infra.exception.UserNotFoundException;
import com.chefia.addresses.model.CreateAddressDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService implements AddressInputPort {

    private final AddressRepositoryOutputPort addressRepositoryOutputPort;
    private final UserRepositoryOutputPort userRepositoryOutputPort;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepositoryOutputPort addressRepositoryOutputPort,
                          UserRepositoryOutputPort userRepositoryOutputPort,
                          AddressMapper addressMapper) {
        this.addressRepositoryOutputPort = addressRepositoryOutputPort;
        this.userRepositoryOutputPort = userRepositoryOutputPort;
        this.addressMapper = addressMapper;
    }

    public AddressDTO createAddressForUser(Long userId, CreateAddressDTO createAddressDTO) {
        var user = Optional.ofNullable(this.userRepositoryOutputPort
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();

        var addressToInsert = this.addressMapper.toCreateAddressEntity(createAddressDTO);
        addressToInsert.setUserId(userId);

        this.addressRepositoryOutputPort.saveAddressForUser(addressToInsert);

        return this.addressMapper.toAddressResponseDTO(addressToInsert);
    }

    public AddressDTO findById(Long id) {
        var address = Optional.ofNullable(this.addressRepositoryOutputPort
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id)));
        assert address.isPresent();
        return this.addressMapper.toAddressResponseDTO(address.get());
    }

    public AddressDTO updateAddress(Long id, UpdateAddressDTO updateAddressDTO) {
        var addressEntity = this.addressRepositoryOutputPort
                .findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id));

        addressEntity.setStreet(updateAddressDTO.getStreet());
        addressEntity.setCity(updateAddressDTO.getCity());
        addressEntity.setState(updateAddressDTO.getState());
        addressEntity.setCountry(updateAddressDTO.getCountry());
        addressEntity.setNumber(updateAddressDTO.getNumber());

        this.addressRepositoryOutputPort.updateAddress(id, updateAddressDTO);
        return addressMapper.toAddressResponseDTO(addressEntity);
    }

    public void deleteAddress(Long id) {
        this.addressRepositoryOutputPort.deleteById(id);
    }
}
