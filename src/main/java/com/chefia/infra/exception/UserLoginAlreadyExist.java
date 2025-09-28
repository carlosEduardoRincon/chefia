package com.chefia.infra.exception;

public class UserLoginAlreadyExist extends RuntimeException {
    public UserLoginAlreadyExist(String message) {
        super(message);
    }
}
