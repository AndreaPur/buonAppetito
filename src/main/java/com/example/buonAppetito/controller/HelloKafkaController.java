package com.example.buonAppetito.controller;

import com.example.buonAppetito.response.OrdineResponse;
import com.example.buonAppetito.producer.KafkaJsonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class HelloKafkaController {

    @Autowired
    private KafkaJsonProducer kafkaJsonProducer;

    @PostMapping("/send-json")
    public ResponseEntity<String> sendMessage(@RequestBody OrdineResponse ordineResponse) {
        kafkaJsonProducer.sendMessage(ordineResponse);
        return new ResponseEntity<>("Messaggio inviato in coda correttamente nel topic hello in formato JSON", HttpStatus.OK);    }

}
