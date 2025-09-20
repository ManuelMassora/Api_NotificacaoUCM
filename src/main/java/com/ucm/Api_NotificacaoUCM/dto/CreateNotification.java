package com.ucm.Api_NotificacaoUCM.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNotification(@NotBlank String titulo, @NotBlank String descricao, long classeId) {
}