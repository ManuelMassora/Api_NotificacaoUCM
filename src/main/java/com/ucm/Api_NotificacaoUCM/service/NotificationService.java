package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.CreateNotification;
import com.ucm.Api_NotificacaoUCM.model.Notification;
import com.ucm.Api_NotificacaoUCM.repo.ClassRepo;
import com.ucm.Api_NotificacaoUCM.repo.NotificationRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final ClassRepo classRepo;

    public NotificationService(NotificationRepo notificationRepo, ClassRepo classRepo) {
        this.notificationRepo = notificationRepo;
        this.classRepo = classRepo;
    }

    @Transactional
    public Notification create(CreateNotification dto) {
        var classe = classRepo.findById(dto.classeId())
                .orElseThrow(() -> new EntityNotFoundException("Classe não encontrada com ID: " + dto.classeId()));

        var notification = new Notification();
        notification.setTitulo(dto.titulo());
        notification.setDescricao(dto.descricao());
        notification.setClassId(classe);
        notification.setDataCriacao(LocalDateTime.now());

        return notificationRepo.save(notification);
    }

    public Optional<Notification> getById(long id) {
        return notificationRepo.findById(id);
    }

    public Page<Notification> getAll(Pageable pageable) {
        return notificationRepo.findAll(pageable);
    }

    public Page<Notification> getAllByClass(long classId, Pageable pageable) {
        return notificationRepo.findAllByClassIdId(classId,pageable);
    }

    @Transactional
    public void delete(long id) {
        if (!notificationRepo.existsById(id)) {
            throw new EntityNotFoundException("Notificação não encontrada com ID: " + id);
        }
        notificationRepo.deleteById(id);
    }

}