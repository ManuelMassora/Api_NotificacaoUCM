package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.LoginRequest;
import com.ucm.Api_NotificacaoUCM.dto.LoginResponse;
import com.ucm.Api_NotificacaoUCM.model.Role;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public AuthService(JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public LoginResponse authenicateCliente(
            @Valid LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var user = userRepo.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais invalidas"));

        if(!user.isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Credenciais invalidas");
        }

        var now = Instant.now();
        var expiredIn = 1L; // 1 Hora de duracao
        var expirationDuration = Duration.ofHours(expiredIn);

        var roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plus(expirationDuration))
                .claim("authorities", roles)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // 1. Calculo de data/hora de expiração
        var expirationInstant = now.plus(expirationDuration);
        var expirationDurationSeconds = expirationDuration.getSeconds();

        // 2. Formate a data para o padrão GMT (RFC 1123)
        String expiresFormatted = DateTimeFormatter
                .RFC_1123_DATE_TIME
                .withZone(ZoneId.of("GMT"))
                .format(expirationInstant);

        // 3. Defina o status 'Secure'
        // IMPORTANTE: Mude para 'true' (Secure) em produção!
        // Usar 'false' é aceitável APENAS em ambiente local (HTTP).
        String secureAttribute = "false"; // "false" (HTTP) ou "true" (HTTPS)

        String cookieHeader = String.format(
                "%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax; Secure=%s; Expires=%s",
                "access_token",
                jwtValue,
                expirationDurationSeconds,
                secureAttribute,
                expiresFormatted
        );

        // 5. Adicione o cabeçalho à resposta HTTP
        response.addHeader("Set-Cookie", cookieHeader);
        return new LoginResponse(null, expiredIn);
    }
}