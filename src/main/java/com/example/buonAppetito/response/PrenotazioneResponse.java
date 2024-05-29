package com.example.buonAppetito.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrenotazioneResponse {

    private Long id;
    private String nomeUtente;
    private String cognomeUtente;
    private String nomeRistorante;
    private String nomeComune;
    private String nTavolo;
    private LocalDateTime orario;
    private LocalDateTime timestamp;

}
