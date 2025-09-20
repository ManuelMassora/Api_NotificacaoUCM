package com.ucm.Api_NotificacaoUCM.dto;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email String email, String password) {
}