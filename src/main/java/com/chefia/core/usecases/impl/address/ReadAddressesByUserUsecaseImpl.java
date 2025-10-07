package com.chefia.core.usecases.impl.address;

import com.chefia.addresses.model.AddressDTO;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.mapper.AddressMapper;
import com.chefia.core.usecases.interfaces.address.ReadAddressesByUserUsecase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ReadAddressesByUserUsecaseImpl implements ReadAddressesByUserUsecase {

    private final AddressGateway addressGateway;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressDTO> execute(Long userId) {
        return this.addressGateway.findByUserId(userId)
                .stream()
                .map(this.addressMapper::toAddressResponseDTO)
                .toList();
    }
}
