package com.chefia.core.port.output.usertype;

import com.chefia.domain.model.UserType;

public interface UserTypeValidatorOutputPort {
    void validate(UserType userType);
}
