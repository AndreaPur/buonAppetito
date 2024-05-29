package com.example.buonAppetito.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TavoloRequest {

    private Long idRistorante;
    private int posti;

}
