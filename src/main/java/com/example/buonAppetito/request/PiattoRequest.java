package com.example.buonAppetito.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PiattoRequest {

    private String nome;
    private Double prezzo;

}
