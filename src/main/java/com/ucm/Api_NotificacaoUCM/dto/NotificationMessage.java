package com.ucm.Api_NotificacaoUCM.dto;

import java.io.Serializable;

public record NotificationMessage(Long id, String titulo, String descricao, Long classId) implements Serializable {}
