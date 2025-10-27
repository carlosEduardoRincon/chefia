package com.chefia.core.exceptions;

public class UserEmailAlreadyExist extends RuntimeException {
    public UserEmailAlreadyExist(String message) {
        super(message);
    }
}
