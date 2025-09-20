package com.ucm.Api_NotificacaoUCM.dto;

import java.util.Date;

public record CreateWorkTask(String titulo, String descricao, Date dataEntrega, long classId) {
}
