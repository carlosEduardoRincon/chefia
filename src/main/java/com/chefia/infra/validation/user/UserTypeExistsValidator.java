package com.chefia.infra.validation.user;

import com.chefia.core.gateway.UserValidatorGateway;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserTypeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserTypeExistsValidator implements UserValidatorGateway {

    private final UserTypeGateway userTypeGateway;

    public UserTypeExistsValidator(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    public void validate(User user) {
        if (userTypeGateway.findById(user.getUserTypeId()).isEmpty()) {
            throw new UserTypeNotFoundException("User Type does nos exist");
        }
    }
}
