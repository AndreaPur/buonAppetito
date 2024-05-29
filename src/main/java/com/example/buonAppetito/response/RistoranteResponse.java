package com.example.buonAppetito.response;

import lombok.*;

import java.time.LocalTime;
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
    private Long comuneId;
    private String comuneNome;
    private LocalTime orarioApertura;
    private LocalTime orarioChiusura;
    private List<Long> menuIds;
    private Long idProprietario;
}