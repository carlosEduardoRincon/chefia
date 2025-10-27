package com.chefia.infra.validation.user;

import com.chefia.core.gateway.UserGateway;
import com.chefia.core.gateway.UserValidatorGateway;
import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserEmailAlreadyExist;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidator implements UserValidatorGateway {

    private final UserGateway userGateway;

    public UserEmailValidator(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void validate(User user) {
        if (userGateway.findByEmailValidation(user.getEmail())) {
            throw new UserEmailAlreadyExist("E-mail already used");
        }
    }
}
