package com.chefia.adapters.user.outputs.validators;

import com.chefia.core.port.output.user.UserRepositoryOutputPort;
import com.chefia.core.port.output.user.UserValidatorOutputPort;
import com.chefia.domain.model.User;
import com.chefia.infra.exception.UserLoginAlreadyExist;
import org.springframework.stereotype.Component;


@Component
public class UserLoginValidator implements UserValidatorOutputPort {

    private final UserRepositoryOutputPort userRepositoryOutputPort;

    public UserLoginValidator(UserRepositoryOutputPort userRepositoryOutputPort) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
    }

    @Override
    public void validate(User user) {
        if (userRepositoryOutputPort.findByLoginValidation(user.getLogin())) {
            throw new UserLoginAlreadyExist("Login already used");
        }
    }
}
