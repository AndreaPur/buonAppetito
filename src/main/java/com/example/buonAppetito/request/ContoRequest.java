package com.example.buonAppetito.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContoRequest {

    private double totale;
    private Long tavoloId;
    private List<Long> ordiniId;

}
