package com.chefia.core.exceptions;

public class UserTypeNameAlreadyExists extends RuntimeException {
    public UserTypeNameAlreadyExists(String message) {
        super(message);
    }
}
