package com.example.buonAppetito.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdineResponse {

    private Long id;
    private Long contoId;
    private String nomePiatto;
    private int quantita;
    private LocalDateTime timestamp;

}
