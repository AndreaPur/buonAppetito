package com.example.buonAppetito.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrdineResponse {

    private Long id;
    private Long prenotazioneId;
    private List<String> piattiNomi;
    private List<Double> piattiPrezzi;
    private double totale;
    private LocalDateTime creazioneOrdine;
    private LocalDateTime chiusuraOrdine;

}