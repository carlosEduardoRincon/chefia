package com.chefia.core.exceptions;

public class UserNotStrongPassword extends RuntimeException {
    public UserNotStrongPassword(String message) {
        super(message);
    }
}
