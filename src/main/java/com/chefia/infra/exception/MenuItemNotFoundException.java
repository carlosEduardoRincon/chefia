package com.chefia.infra.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String message){
        super(message);
    }
}
