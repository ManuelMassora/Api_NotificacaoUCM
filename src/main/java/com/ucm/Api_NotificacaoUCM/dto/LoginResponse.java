package com.ucm.Api_NotificacaoUCM.dto;

import java.util.List;

public record LoginResponse(List<String> userRoles, Long expiresIn) {
}