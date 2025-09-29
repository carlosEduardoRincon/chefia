package com.chefia.infra.exception;

public class UserTypeNameAlreadyExists extends RuntimeException {
    public UserTypeNameAlreadyExists(String message) {
        super(message);
    }
}
