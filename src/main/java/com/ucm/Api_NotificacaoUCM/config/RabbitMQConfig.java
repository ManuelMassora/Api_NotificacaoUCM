package com.ucm.Api_NotificacaoUCM.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "notificacao.queue";
    public static final String EXCHANGE_NAME = "notificacao.exchange";
    public static final String ROUTING_KEY = "notificacao.key";

    @Bean
    public Queue notificacaoQueue() {
        // durable=true => persiste mesmo se o RabbitMQ reiniciar
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange notificacaoExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding notificacaoBinding(Queue notificacaoQueue, DirectExchange notificacaoExchange) {
        return BindingBuilder.bind(notificacaoQueue).to(notificacaoExchange).with(ROUTING_KEY);
    }
}