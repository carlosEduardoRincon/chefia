package com.chefia.infra.validation.usertype;

import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.gateway.UserTypeValidatorGateway;
import com.chefia.core.entities.UserType;
import com.chefia.core.exceptions.UserTypeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserTypeNameValidator implements UserTypeValidatorGateway {

    private final UserTypeGateway userTypeGateway;

    public UserTypeNameValidator(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    public void validate(UserType userType) {
        if (userTypeGateway.findByName(userType.getName()).isPresent()) {
            throw new UserTypeNotFoundException("User Type already exist");
        }
    }
}
