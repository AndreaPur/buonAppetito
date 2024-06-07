package com.example.buonAppetito.producer;

import com.example.buonAppetito.response.OrdineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaJsonProducer {

    @Autowired
    private KafkaTemplate<String, OrdineResponse> kafkaTemplate;

    public void sendMessage(OrdineResponse ordineResponse) {
        Message<OrdineResponse> message = MessageBuilder
                .withPayload(ordineResponse)
                .setHeader(KafkaHeaders.TOPIC, "hello")
                .build();
        kafkaTemplate.send(message);
    }

}
