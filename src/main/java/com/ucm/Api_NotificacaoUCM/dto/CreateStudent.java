package com.ucm.Api_NotificacaoUCM.dto;

import jakarta.validation.constraints.Email;

public record CreateStudent(@Email String email, String nome, String senha, String studentNumber, long cursoid, int anoacademico) {
}
