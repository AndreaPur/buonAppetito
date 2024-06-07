package com.example.buonAppetito.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidOrderException extends Exception {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}