package com.chefia.validation;

import com.chefia.entities.User;
import com.chefia.repositories.UserRepository;
import com.chefia.services.exceptions.UserEmailAlreadyExist;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserRepository userRepository;
    private final StrongPasswordValidator strongPasswordValidator;

    public UserValidator(
            UserRepository userRepository,
            StrongPasswordValidator strongPasswordValidator
    ) {
        this.userRepository = userRepository;
        this.strongPasswordValidator = strongPasswordValidator;
    }

    public void validateUser(User userToInsert) {
        this.userRepository.findByEmail(userToInsert.getEmail())
                .ifPresent(user -> {
                    throw new UserEmailAlreadyExist("User already exist with this email");
                });

        this.strongPasswordValidator.validateStrongPassword(userToInsert.getPassword());
    }
}
