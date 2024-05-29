package com.example.buonAppetito.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProprietarioRequest {

    private String nome;
    private String cognome;
    private String email;
    private String nomeComune;
    private String indirizzo;
    private String telefono;
    private LocalDate dataNascita;
    private String password;
    private List<Long> idRistoranti;

}
