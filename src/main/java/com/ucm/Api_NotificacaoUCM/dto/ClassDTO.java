package com.ucm.Api_NotificacaoUCM.dto;

import java.time.Year;

public record ClassDTO(long id, String nome, String docente, String descricao, int ano, Year anoLetivo, String curso) {
}