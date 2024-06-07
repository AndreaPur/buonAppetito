package com.example.buonAppetito.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProprietarioResponse {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String nomeComune;
    private String indirizzo;
    private String telefono;
    private LocalDate dataNascita;
    private List<Long> idRistoranti;
    private List<String> nomiRistoranti;

}
