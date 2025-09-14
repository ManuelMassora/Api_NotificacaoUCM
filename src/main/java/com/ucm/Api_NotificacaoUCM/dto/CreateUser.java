package com.ucm.Api_NotificacaoUCM.dto;

import jakarta.validation.constraints.Email;

public record CreateUser(@Email String email, String nome, String senha) {
}
