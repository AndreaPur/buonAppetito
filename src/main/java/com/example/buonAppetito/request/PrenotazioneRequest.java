package com.example.buonAppetito.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrenotazioneRequest {

    private Long utenteId;
    private Long ristoranteId;
    private Long tavoloId;
    private Long posti;
    private LocalDateTime orario;

}
