package com.example.buonAppetito.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponse {

    private Long id;
    private String nome;
    private List<Long> piattiId;
    private List<String> piattiNomi;
    private List<Double> piattiPrezzi;
    private String nomeRistorante;

}
