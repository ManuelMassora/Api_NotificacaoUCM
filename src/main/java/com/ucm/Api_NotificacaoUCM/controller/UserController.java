package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.CreateStudent;
import com.ucm.Api_NotificacaoUCM.dto.CreateUser;
import com.ucm.Api_NotificacaoUCM.dto.LoginRequest;
import com.ucm.Api_NotificacaoUCM.dto.LoginResponse;
import com.ucm.Api_NotificacaoUCM.service.AuthService;
import com.ucm.Api_NotificacaoUCM.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Void> newStudent(@Valid @RequestBody CreateStudent createStudent) {
        userService.CreateStudent(createStudent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cadastrarprof")
    @PreAuthorize("hasAnyAuthority('admin','professor')")
    public ResponseEntity<Void> newProfessor(@Valid @RequestBody CreateUser createUser) {
        userService.CreateTeacher(createUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> auth(@Valid @RequestBody LoginRequest loginRequest) {
        var response = authService.authenicateCliente(loginRequest);
        return ResponseEntity.ok(response);
    }
}
