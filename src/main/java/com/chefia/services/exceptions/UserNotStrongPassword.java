package com.chefia.services.exceptions;

public class UserNotStrongPassword extends RuntimeException {
    public UserNotStrongPassword(String message) {
        super(message);
    }
}
