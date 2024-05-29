package com.example.buonAppetito.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String nomeComune;
    private String indirizzo;
    private String telefono;
    private LocalDate dataNascita;

}
