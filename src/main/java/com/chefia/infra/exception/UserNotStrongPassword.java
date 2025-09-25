package com.chefia.infra.exception;

public class UserNotStrongPassword extends RuntimeException {
    public UserNotStrongPassword(String message) {
        super(message);
    }
}
