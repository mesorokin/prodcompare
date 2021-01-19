package com.sorokin.consumer;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumer {


    @KafkaListener(topics = "product-updates", groupId = "product-comparison-service")
    public void consume(String message) {
        throw new UnsupportedOperationException();
    }
}