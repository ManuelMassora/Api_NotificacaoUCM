package com.ucm.Api_NotificacaoUCM.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record WorkTaskDTO(long id, String titulo, String descricao, Date dataEntrega, LocalDateTime dataCriacao, ClassDTO classe) {
}