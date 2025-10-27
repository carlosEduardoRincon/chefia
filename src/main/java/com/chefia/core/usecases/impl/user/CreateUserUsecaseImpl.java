package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.gateway.UserValidatorGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.core.usecases.interfaces.user.CreateUserUsecase;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CreateUserUsecaseImpl implements CreateUserUsecase {

    private final UserGateway userGateway;
    private final List<UserValidatorGateway> userValidatorGateways;
    private final UserMapper userMapper;

    @Override
    public UserDTO execute(CreateUserDTO createUserDTO) {
        var userToInsert = this.userMapper.toEntity(createUserDTO);

        this.validateUser(userToInsert);

        var userId = this.userGateway.save(userToInsert);
        userToInsert.setNrSeqUser(userId);

        return this.userMapper.toUserResponseDTO(userToInsert);
    }

    private void validateUser(User user) {
        for (UserValidatorGateway userValidator : userValidatorGateways) {
            userValidator.validate(user);
        }
    }
}
