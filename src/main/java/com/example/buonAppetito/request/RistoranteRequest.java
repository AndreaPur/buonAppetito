package com.example.buonAppetito.request;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RistoranteRequest {

    private String nome;
    private String indirizzo;
    private Long comuneId;
    private Long postiTotali;
    private LocalDateTime orarioApertura;
    private LocalDateTime orarioChiusura;
}
