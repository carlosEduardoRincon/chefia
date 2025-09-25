package com.chefia.infra.exception;

public class PasswordAlreadyUsed extends RuntimeException {
    public PasswordAlreadyUsed(String message) {
        super(message);
    }
}
