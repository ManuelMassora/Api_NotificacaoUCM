package com.ucm.Api_NotificacaoUCM.dto;

import com.ucm.Api_NotificacaoUCM.model.Curso;

public record StudentDTO(UserDTO user, long id, String studentNumber, int anoAcademindo, Curso curso) {
}