package com.example.buonAppetito.exceptions;

public class UserNotConformedException extends Exception{

    @Override
    public String getMessage() {
        return "Devi confermare il link via email per poter accedere!";
    }
}
