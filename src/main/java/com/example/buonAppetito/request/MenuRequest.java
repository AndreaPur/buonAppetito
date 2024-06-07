package com.example.buonAppetito.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequest {

    private String nome;
    private List<Long> piattiId;
    private Long ristoranteId;

}