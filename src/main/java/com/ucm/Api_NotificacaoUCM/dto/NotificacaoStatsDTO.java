package com.ucm.Api_NotificacaoUCM.dto;

public record NotificacaoStatsDTO(
        long total,
        long lidas,
        long naoLidas,
        double percentualLeitura
) {}