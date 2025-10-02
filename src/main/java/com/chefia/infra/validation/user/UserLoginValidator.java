package com.chefia.infra.validation.user;

import com.chefia.core.gateway.UserGateway;
import com.chefia.core.gateway.UserValidatorGateway;
import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserLoginAlreadyExist;
import org.springframework.stereotype.Component;


@Component
public class UserLoginValidator implements UserValidatorGateway {

    private final UserGateway userGateway;

    public UserLoginValidator(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void validate(User user) {
        if (userGateway.findByLoginValidation(user.getLogin())) {
            throw new UserLoginAlreadyExist("Login already used");
        }
    }
}
