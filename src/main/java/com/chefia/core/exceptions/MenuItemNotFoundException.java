package com.chefia.core.exceptions;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String message){
        super(message);
    }
}
