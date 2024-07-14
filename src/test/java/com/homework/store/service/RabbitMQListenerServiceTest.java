package com.homework.store.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class RabbitMQListenerServiceTest {

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management");

    @Autowired
    private RabbitMQListenerService listenerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeAll
    static void setUp() {
        rabbitMQContainer.start();
    }

    @AfterAll
    static void tearDown() {
        rabbitMQContainer.stop();
    }

    @DynamicPropertySource
    static void registerRabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void testRabbitMQListenerService() {
        String testMessage = "Test message";
        rabbitTemplate.convertAndSend("http-trace", testMessage);

        Assertions.assertEquals(testMessage, listenerService.returner(testMessage));
    }

}