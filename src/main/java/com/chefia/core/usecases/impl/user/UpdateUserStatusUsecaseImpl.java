package com.chefia.core.usecases.impl.user;

import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.usecases.interfaces.user.UpdateUserStatusUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateUserStatusUsecaseImpl implements UpdateUserStatusUsecase {

    private final UserGateway userGateway;

    @Override
    public void execute(Long userId, Boolean status) {
        var userEntity = this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userEntity.setActive(status);
        userEntity.updatedAt();

        this.userGateway.updateUserStatus(userId, userEntity);
    }
}
