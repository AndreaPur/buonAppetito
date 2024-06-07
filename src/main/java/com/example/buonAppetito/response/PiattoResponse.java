package com.example.buonAppetito.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PiattoResponse {

    private Long id;
    private String nome;
    private Double prezzo;

}
