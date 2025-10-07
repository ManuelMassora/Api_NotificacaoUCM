package com.ucm.Api_NotificacaoUCM.dto;

import java.time.LocalDateTime;

public record NotificationWithStatusDTO(long id,
                                        String titulo,
                                        String descricao,
                                        LocalDateTime dataCriacao,
                                        boolean lida,
                                        LocalDateTime dataLeitura,
                                        ClassDTO classe) {
}
