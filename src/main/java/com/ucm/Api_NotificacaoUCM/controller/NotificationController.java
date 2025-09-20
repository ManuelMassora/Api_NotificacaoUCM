package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.CreateNotification;
import com.ucm.Api_NotificacaoUCM.dto.NotificationDTO;
import com.ucm.Api_NotificacaoUCM.model.Notification;
import com.ucm.Api_NotificacaoUCM.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<NotificationDTO> create(@RequestBody @Valid CreateNotification dto, UriComponentsBuilder uriBuilder) {
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
    public ResponseEntity<Page<NotificationDTO>> getAllByClass(@PathVariable long classId, @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable pageable) {
        try {
            Page<NotificationDTO> notifications = notificationService.getAllByClass(classId, pageable);
            return ResponseEntity.ok(notifications);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
