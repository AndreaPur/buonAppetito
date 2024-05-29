package com.example.buonAppetito.exceptions;

public class ComuneNotFoundException extends Exception{

    @Override
    public String getMessage() {
        return "Sei sull'isola che non c'è";
    }
}
