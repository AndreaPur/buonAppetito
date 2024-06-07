package com.example.buonAppetito.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdineRequest {

    private Long prenotazioneId;
    private List<Long> piattiId;
}