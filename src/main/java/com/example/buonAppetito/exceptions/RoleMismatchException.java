package com.example.buonAppetito.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleMismatchException extends Exception {

    public RoleMismatchException(Long id, String message) {
        super("Utente con id " + id + ": " + message);
    }

}