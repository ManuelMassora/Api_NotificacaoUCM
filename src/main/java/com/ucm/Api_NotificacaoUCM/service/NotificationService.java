package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.ClassDTO;
import com.ucm.Api_NotificacaoUCM.dto.CreateNotification;
import com.ucm.Api_NotificacaoUCM.dto.NotificationDTO;
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
    public NotificationDTO create(CreateNotification dto) {
        var classe = classRepo.findById(dto.classeId())
                .orElseThrow(() -> new EntityNotFoundException("Classe não encontrada com ID: " + dto.classeId()));

        var notification = new Notification();
        notification.setTitulo(dto.titulo());
        notification.setDescricao(dto.descricao());
        notification.setClassId(classe);
        notification.setDataCriacao(LocalDateTime.now());

        var notificationSave = notificationRepo.save(notification);

        return new NotificationDTO(
                notificationSave.getId(),
                notificationSave.getTitulo(),
                notificationSave.getDescricao(),
                notificationSave.getDataCriacao(),
                new ClassDTO(
                        notificationSave.getClassId().getId(),
                        notificationSave.getClassId().getNome(),
                        notificationSave.getClassId().getDocente().getName(),
                        notificationSave.getClassId().getDescricao(),
                        notificationSave.getClassId().getAno(),
                        notificationSave.getClassId().getAnoLetivo()
                )
        );
    }

    public NotificationDTO getById(long id) {
        var notification = notificationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notiacao nao encontrada"));
        var classDto = new ClassDTO(
                notification.getClassId().getId(),
                notification.getClassId().getNome(),
                notification.getClassId().getDocente().getName(),
                notification.getClassId().getDescricao(),
                notification.getClassId().getAno(),
                notification.getClassId().getAnoLetivo()
        );
        return new NotificationDTO(
                notification.getId(),
                notification.getTitulo(),
                notification.getDescricao(),
                notification.getDataCriacao(),
                classDto
        );
    }

    public Page<NotificationDTO> getAll(Pageable pageable) {
        return notificationRepo.findAll(pageable).map(notification -> new NotificationDTO(
                notification.getId(),
                notification.getTitulo(),
                notification.getDescricao(),
                notification.getDataCriacao(),
                new ClassDTO(
                        notification.getClassId().getId(),
                        notification.getClassId().getNome(),
                        notification.getClassId().getDocente().getName(),
                        notification.getClassId().getDescricao(),
                        notification.getClassId().getAno(),
                        notification.getClassId().getAnoLetivo()
                )
        ));
    }

    public Page<NotificationDTO> getAllByClass(long classId, Pageable pageable) {
        return notificationRepo.findAllByClassIdId(classId,pageable).map(notification -> new NotificationDTO(
                notification.getId(),
                notification.getTitulo(),
                notification.getDescricao(),
                notification.getDataCriacao(),
                new ClassDTO(
                        notification.getClassId().getId(),
                        notification.getClassId().getNome(),
                        notification.getClassId().getDocente().getName(),
                        notification.getClassId().getDescricao(),
                        notification.getClassId().getAno(),
                        notification.getClassId().getAnoLetivo()
                )
        ));
    }

    @Transactional
    public void delete(long id) {
        if (!notificationRepo.existsById(id)) {
            throw new EntityNotFoundException("Notificação não encontrada com ID: " + id);
        }
        notificationRepo.deleteById(id);
    }

}