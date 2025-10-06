package com.chefia.core.usecases.impl.user;

import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.core.usecases.interfaces.user.UpdateUserUsecase;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class UpdateUserUsecaseImpl implements UpdateUserUsecase {

    private final UserGateway userGateway;
    private final UserMapper userMapper;

    @Override
    public UserDTO execute(Long userId, UpdateUserDTO body) {
        var userEntity = this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userEntity.setName(body.getName());
        userEntity.setEmail(body.getEmail());
        userEntity.setLogin(body.getLogin());
        userEntity.setUpdatedAt(LocalDateTime.now());

        this.userGateway.update(userId, userEntity);

        return this.userMapper.toUserResponseDTO(userEntity);
    }
}
