package com.chefia.validation;

import com.chefia.services.exceptions.UserNotStrongPassword;
import org.springframework.stereotype.Component;

@Component
public class StrongPasswordValidator {
    public void validateStrongPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (password != null && password.matches(regex)) return;
        throw new UserNotStrongPassword("Password not strong enough");
    }
}
