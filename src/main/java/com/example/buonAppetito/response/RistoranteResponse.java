package com.example.buonAppetito.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RistoranteResponse {

    private Long id;
    private String nome;
    private String indirizzo;
    private String comuneNome;
    private Long postiTotali;
    private Long postiDisponibili;
    private LocalDateTime orarioApertura;
    private LocalDateTime orarioChiusura;
    private List<Long> menuId;
}
