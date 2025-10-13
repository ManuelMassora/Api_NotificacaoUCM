package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.LoginRequest;
import com.ucm.Api_NotificacaoUCM.dto.LoginResponse;
import com.ucm.Api_NotificacaoUCM.model.Role;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!user.isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        var now = Instant.now();
        var expiredIn = 1L; // 1 hora
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

        // Cookie para ambiente de desenvolvimento (HTTP)
        ResponseCookie cookie = ResponseCookie.from("access_token", jwtValue)
                .httpOnly(true)
                .secure(true) // Desativado para desenvolvimento (HTTP)
                .sameSite("None") // Compatível com HTTP e ambiente local
                .path("/")
                .maxAge(expirationDuration) // Corrigido para segundos
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Log para depuração
        System.out.println("Cookie gerado: " + cookie.toString());
        System.out.println("Max-Age (segundos): " + expirationDuration.getSeconds());

        return new LoginResponse(roles, expiredIn);
    }
}