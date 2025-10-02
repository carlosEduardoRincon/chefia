package com.chefia.core.exceptions;

public class PasswordAlreadyUsed extends RuntimeException {
    public PasswordAlreadyUsed(String message) {
        super(message);
    }
}
