package com.example.buonAppetito.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdineRequest {

    private Long contoId;
    private Long piattoId;
    private int quantita;

}
