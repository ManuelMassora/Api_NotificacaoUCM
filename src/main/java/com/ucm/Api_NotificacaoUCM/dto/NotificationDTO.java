package com.ucm.Api_NotificacaoUCM.dto;

import java.time.LocalDateTime;

public record NotificationDTO(long id, String titulo, String descricao, LocalDateTime dataCriacao, ClassDTO classe) {
}
