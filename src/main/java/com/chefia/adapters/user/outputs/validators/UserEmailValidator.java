package com.chefia.adapters.user.outputs.validators;

import com.chefia.core.port.output.user.UserRepositoryOutputPort;
import com.chefia.core.port.output.user.UserValidatorOutputPort;
import com.chefia.domain.model.User;
import com.chefia.infra.exception.UserEmailAlreadyExist;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidator implements UserValidatorOutputPort {

    private final UserRepositoryOutputPort userRepositoryOutputPort;

    public UserEmailValidator(UserRepositoryOutputPort userRepositoryOutputPort) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
    }

    @Override
    public void validate(User user) {
        if (userRepositoryOutputPort.findByEmailValidation(user.getEmail())) {
            throw new UserEmailAlreadyExist("E-mail already used");
        }
    }
}
