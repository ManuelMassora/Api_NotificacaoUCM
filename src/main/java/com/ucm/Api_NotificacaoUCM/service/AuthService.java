package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.LoginRequest;
import com.ucm.Api_NotificacaoUCM.dto.LoginResponse;
import com.ucm.Api_NotificacaoUCM.model.Role;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
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

    public LoginResponse authenicateCliente(@Valid LoginRequest loginRequest) {
        var user = userRepo.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais invalidas"));

        if(!user.isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Credenciais invalidas");
        }

        var now = Instant.now();
        var expiredIn = 1L; // 1 Hora de duracao

        var roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofHours(expiredIn)))
                .claim("authorities", roles)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiredIn);
    }
}