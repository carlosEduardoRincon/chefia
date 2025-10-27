package com.chefia.core.exceptions;

public class PasswordNotMatch extends RuntimeException {
    public PasswordNotMatch(String message) {
        super(message);
    }
}
