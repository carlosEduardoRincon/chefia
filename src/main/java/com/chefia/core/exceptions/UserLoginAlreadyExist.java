package com.chefia.core.exceptions;

public class UserLoginAlreadyExist extends RuntimeException {
    public UserLoginAlreadyExist(String message) {
        super(message);
    }
}
