package com.example.buonAppetito.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {

    private String nome;
    private String cognome;
    private String email;
    private String nomeComune;
    private String indirizzo;
    private String telefono;
    private LocalDate dataNascita;
    private String password;

}
