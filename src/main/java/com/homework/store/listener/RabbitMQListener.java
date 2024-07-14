package com.homework.store.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

//@Service
public class RabbitMQListener {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQListener.class);

 //   @RabbitListener(queues = "http-trace")
    public void listen(String message) {
        log.info("Received message: {}", message);
        returner(message);
    }

    // This method is used to print the message ONLY IN TESTS
    protected String returner(String message) {
        return message;
    }
}
