package com.chefia.core.usecases.impl.user;

import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.core.usecases.interfaces.user.ReadUserUsecase;
import com.chefia.users.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ReadUserUsecaseImpl implements ReadUserUsecase {

    private final UserGateway userGateway;
    private final UserMapper userMapper;

    @Override
    public UserDTO execute(Long userId) {
        var user = Optional.ofNullable(this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        assert user.isPresent();
        return this.userMapper.toUserResponseDTO(user.get());
    }
}
