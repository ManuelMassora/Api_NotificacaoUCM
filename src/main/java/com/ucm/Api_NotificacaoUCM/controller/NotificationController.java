package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.CreateNotification;
import com.ucm.Api_NotificacaoUCM.dto.NotificacaoStatsDTO;
import com.ucm.Api_NotificacaoUCM.dto.NotificationDTO;
import com.ucm.Api_NotificacaoUCM.dto.NotificationWithStatusDTO;
import com.ucm.Api_NotificacaoUCM.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<NotificationDTO> create(@RequestBody @Valid CreateNotification dto) {
        try {
            var createdNotification = notificationService.create(dto);
            return ResponseEntity.ok().body(createdNotification);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable long id) {
        NotificationDTO notification = notificationService.getById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<Page<NotificationDTO>> getAll(@PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable pageable) {
        Page<NotificationDTO> notifications = notificationService.getAll(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/by-class/{classId}")
    public ResponseEntity<Page<NotificationDTO>> getAllByClass(
            @PathVariable long classId,
            @RequestParam(required = false) String titulo,
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable pageable) {
        try {
            Page<NotificationDTO> notifications = notificationService.getAllByClass(classId, titulo, pageable);
            return ResponseEntity.ok(notifications);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-class-status/{classId}")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Page<NotificationWithStatusDTO>> getAllByClassWithStatus(
            JwtAuthenticationToken token,
            @PathVariable long classId,
            @RequestParam(required = false) String titulo,
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable pageable) {
        try {
            Page<NotificationWithStatusDTO> notifications = notificationService.getAllByClassAndStatus(token, classId, titulo, pageable);
            return ResponseEntity.ok(notifications);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("lido/{id}")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Void> marcarComoLido(JwtAuthenticationToken token, @PathVariable long id) {
        try {
            notificationService.marcarComoLida(token, id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("nao-lidas/total")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Long> getNaoLidasTotal(JwtAuthenticationToken token) {
        return ResponseEntity.ok(notificationService.contarNaoLidasTotais(token));
    }

    @GetMapping("nao-lidas/turma/{classId}")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Long> getNaoLidasPorTurma(JwtAuthenticationToken token, @PathVariable long classId) {
        return ResponseEntity.ok(notificationService.contarNaoLidasPorTurma(token, classId));
    }

    @GetMapping("/turmas/{classId}/estatisticas")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<NotificacaoStatsDTO> getEstatisticas(@PathVariable long classId) {
        return ResponseEntity.ok(notificationService.getEstatisticasPorTurma(classId));
    }

    @PatchMapping("marcar-todas-lidas")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<String> marcarTodasComoLidas(
            JwtAuthenticationToken token) {
        int atualizadas = notificationService.marcarTodasComoLidas(token);
        return ResponseEntity.ok(atualizadas + " notificações marcadas como lidas");
    }

    @PatchMapping("turmas/{classId}/marcar-todas-lidas")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<String> marcarTodasComoLidasPorTurma(
            @PathVariable long classId,
            JwtAuthenticationToken token) {
        int atualizadas = notificationService.marcarTodasComoLidasPorTurma(token, classId);
        return ResponseEntity.ok(atualizadas + " notificações marcadas como lidas");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        try {
            notificationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
