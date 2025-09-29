package com.chefia.adapters.usertype.outputs.validators;

import com.chefia.core.port.output.usertype.UserTypeRepositoryOutputPort;
import com.chefia.core.port.output.usertype.UserTypeValidatorOutputPort;
import com.chefia.domain.model.User;
import com.chefia.domain.model.UserType;
import com.chefia.infra.exception.UserTypeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserTypeNameValidator implements UserTypeValidatorOutputPort {

    private final UserTypeRepositoryOutputPort userTypeRepositoryOutputPort;

    public UserTypeNameValidator(UserTypeRepositoryOutputPort userTypeRepositoryOutputPort) {
        this.userTypeRepositoryOutputPort = userTypeRepositoryOutputPort;
    }

    @Override
    public void validate(UserType userType) {
        if (!userTypeRepositoryOutputPort.findByName(userType.getName()).isEmpty()) {
            throw new UserTypeNotFoundException("User Type already exist");
        }
    }
}
