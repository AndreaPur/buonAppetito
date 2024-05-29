package com.example.buonAppetito.request;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RistoranteRequest {

    private String nome;
    private String indirizzo;
    private Long comuneId;
    private LocalTime orarioApertura;
    private LocalTime orarioChiusura;
    private List<Long> menuIds;
    private Long idProprietario;
}
