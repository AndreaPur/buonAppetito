package com.example.buonAppetito.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TavoloResponse {

    private Long id;
    private String nomeRistorante;
    private int posti;
}
