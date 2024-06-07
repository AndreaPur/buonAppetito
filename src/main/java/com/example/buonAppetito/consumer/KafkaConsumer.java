package com.example.buonAppetito.consumer;

import com.example.buonAppetito.response.OrdineResponse;
import com.example.buonAppetito.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    private FileService fileService;

    @KafkaListener(topics = "hello", groupId = "academy2024")
    public void consumeJsonMessage(OrdineResponse ordineResponse) {
        log.info(String.format("messaggio arrivato a destinazione dal topic hello: %s", ordineResponse.toString()));
        fileService.writeToFile("src/main/resources/scontrini.txt", ordineResponse.toString());
    }

}
