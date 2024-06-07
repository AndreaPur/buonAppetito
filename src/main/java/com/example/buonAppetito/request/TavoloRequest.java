package com.example.buonAppetito.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TavoloRequest {

    private Long ristoranteId;
    private Long posti;

}
