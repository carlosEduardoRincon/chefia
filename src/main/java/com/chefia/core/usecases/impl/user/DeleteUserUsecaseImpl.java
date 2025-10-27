package com.chefia.core.usecases.impl.user;

import com.chefia.core.gateway.UserGateway;
import com.chefia.core.usecases.interfaces.user.DeleteUserUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeleteUserUsecaseImpl implements DeleteUserUsecase {

    private final UserGateway userGateway;

    @Override
    public void execute(Long userId) {
        this.userGateway.deleteById(userId);
    }
}
