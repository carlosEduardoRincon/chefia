package com.chefia.core.port.output.user;

import com.chefia.domain.model.User;

public interface UserValidatorOutputPort {
    void validate(User user);
}
