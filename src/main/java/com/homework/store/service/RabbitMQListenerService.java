package com.homework.store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListenerService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQListenerService.class);

    @RabbitListener(queues = "http-trace")
    public void listen(String message) {
        log.info("Received message: {}", message);
    }
}
