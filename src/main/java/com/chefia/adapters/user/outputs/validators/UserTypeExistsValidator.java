package com.chefia.adapters.user.outputs.validators;

import com.chefia.core.port.output.user.UserValidatorOutputPort;
import com.chefia.core.port.output.usertype.UserTypeRepositoryOutputPort;
import com.chefia.domain.model.User;
import com.chefia.infra.exception.UserTypeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserTypeExistsValidator implements UserValidatorOutputPort {

    private final UserTypeRepositoryOutputPort userTypeRepositoryOutputPort;

    public UserTypeExistsValidator(UserTypeRepositoryOutputPort userTypeRepositoryOutputPort) {
        this.userTypeRepositoryOutputPort = userTypeRepositoryOutputPort;
    }

    @Override
    public void validate(User user) {
        if (userTypeRepositoryOutputPort.findById(user.getUserTypeId()).isEmpty()) {
            throw new UserTypeNotFoundException("User Type does nos exist");
        }
    }
}
