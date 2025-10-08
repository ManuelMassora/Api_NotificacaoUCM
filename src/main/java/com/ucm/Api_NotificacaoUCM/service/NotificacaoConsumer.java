package com.ucm.Api_NotificacaoUCM.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucm.Api_NotificacaoUCM.config.RabbitMQConfig;
import com.ucm.Api_NotificacaoUCM.dto.NotificationMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoConsumer {

    private final EnviarNotificacaoParaNavedador envioService; // pode ser WebSocket, Email, Push, etc.

    public NotificacaoConsumer(EnviarNotificacaoParaNavedador envioService) {
        this.envioService = envioService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receberMensagem(String json) throws JsonProcessingException {
        NotificationMessage msg = new ObjectMapper().readValue(json, NotificationMessage.class);
        envioService.enviarParaAlunos(msg);
    }
}
