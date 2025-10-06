package com.chefia.core.usecases.impl.address;

import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.usecases.interfaces.address.DeleteAddressUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeleteAddressUsecaseImpl implements DeleteAddressUsecase {

    private final AddressGateway addressGateway;

    @Override
    public void execute(Long addressId) {
        this.addressGateway.deleteById(addressId);
    }
}
