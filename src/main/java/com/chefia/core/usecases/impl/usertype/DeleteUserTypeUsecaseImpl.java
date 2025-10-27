package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.usecases.interfaces.usertype.DeleteUserTypeUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeleteUserTypeUsecaseImpl implements DeleteUserTypeUsecase {

    private final UserTypeGateway userTypeGateway;

    @Override
    public void execute(Long userTypeId) {
        this.userTypeGateway.deleteById(userTypeId);
    }
}
