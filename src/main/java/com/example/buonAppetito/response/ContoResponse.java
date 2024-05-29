package com.example.buonAppetito.response;

import com.example.buonAppetito.entities.Ordine;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContoResponse {

    private Long id;
    private double totale;
    private Long tavoloId;
    private List<Ordine> ordini;
    private LocalDateTime timestamp;

}
