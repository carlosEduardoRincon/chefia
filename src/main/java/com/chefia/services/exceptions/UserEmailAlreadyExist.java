package com.chefia.services.exceptions;

public class UserEmailAlreadyExist extends RuntimeException {
    public UserEmailAlreadyExist(String message) {
        super(message);
    }
}
