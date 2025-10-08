package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnviarNotificacaoParaNavedador {

    private final SimpMessagingTemplate messagingTemplate;

    public EnviarNotificacaoParaNavedador(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void enviarParaAlunos(NotificationMessage message) {
        // Envia para um canal específico da turma
        messagingTemplate.convertAndSend(
                "/topic/turma/" + message.classId(),
                message
        );
    }
}
