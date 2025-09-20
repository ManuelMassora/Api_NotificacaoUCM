package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.CreateStudent;
import com.ucm.Api_NotificacaoUCM.dto.CreateUser;
import com.ucm.Api_NotificacaoUCM.dto.LoginRequest;
import com.ucm.Api_NotificacaoUCM.dto.LoginResponse;
import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.model.User;
import com.ucm.Api_NotificacaoUCM.service.AuthService;
import com.ucm.Api_NotificacaoUCM.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/perfil")
    @PreAuthorize("hasAnyAuthority('admin','professor')")
    public ResponseEntity<User> MyPerfil(JwtAuthenticationToken token) {
        try {
            var user = userService.MyProfile(token);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        var user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("user")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Page<User>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        var users = userService.getAllUser(pageable);
        return ResponseEntity.ok(users);
    }
}
