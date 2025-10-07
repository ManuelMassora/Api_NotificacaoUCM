package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.ClassDTO;
import com.ucm.Api_NotificacaoUCM.dto.CreateNotification;
import com.ucm.Api_NotificacaoUCM.dto.NotificationDTO;
import com.ucm.Api_NotificacaoUCM.dto.NotificationWithStatusDTO;
import com.ucm.Api_NotificacaoUCM.dto.NotificacaoStatsDTO;
import com.ucm.Api_NotificacaoUCM.model.NotificacaoStatus;
import com.ucm.Api_NotificacaoUCM.model.NotificacaoStatusId;
import com.ucm.Api_NotificacaoUCM.model.Notification;
import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.repo.ClassRepo;
import com.ucm.Api_NotificacaoUCM.repo.NoticacaoStatusRepository;
import com.ucm.Api_NotificacaoUCM.repo.NotificationRepo;
import com.ucm.Api_NotificacaoUCM.repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final ClassRepo classRepo;
    private final StudentRepo studentRepo;
    private final NoticacaoStatusRepository noticacaoStatusRepository;

    public NotificationService(NotificationRepo notificationRepo, ClassRepo classRepo, StudentRepo studentRepo, NoticacaoStatusRepository noticacaoStatusRepository) {
        this.notificationRepo = notificationRepo;
        this.classRepo = classRepo;
        this.studentRepo = studentRepo;
        this.noticacaoStatusRepository = noticacaoStatusRepository;
    }

    @Transactional
    public NotificationDTO create(CreateNotification dto) {
        var classe = classRepo.findByIdWithDocenteAndCurso(dto.classeId())
                .orElseThrow(() -> new EntityNotFoundException("Classe não encontrada com ID: " + dto.classeId()));
        var notification = new Notification();
        notification.setTitulo(dto.titulo());
        notification.setDescricao(dto.descricao());
        notification.setClassId(classe);
        notification.setDataCriacao(LocalDateTime.now());
        var notificationSave = notificationRepo.save(notification);
        List<Student> estudantes = studentRepo.findAllByClasses_Id(notificationSave.getClassId().getId());
        List<NotificacaoStatus> listaStatus = estudantes.stream()
                .map(estudante -> {
                    NotificacaoStatus status = new NotificacaoStatus();
                    status.setStudent(estudante);
                    status.setNotificacao(notificationSave);
                    System.out.println("ID da notificação: " + notificationSave.getId());
                    System.out.println("ID do estudante: " + estudante.getId());
                    return status;
                })
                .toList();
        System.out.println("Estudantes encontrados: " + estudantes.size());
        noticacaoStatusRepository.saveAll(listaStatus);
        noticacaoStatusRepository.flush();
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
                        notificationSave.getClassId().getAnoLetivo(),
                        notificationSave.getClassId().getCurso().getNome()
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
                notification.getClassId().getAnoLetivo(),
                notification.getClassId().getCurso().getNome()
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
                        notification.getClassId().getAnoLetivo(),
                        notification.getClassId().getCurso().getNome()
                )
        ));
    }

    public Page<NotificationDTO> getAllByClass(long classId, String titulo, Pageable pageable) {
        if (titulo == null || titulo.isBlank()) {
            titulo = "";
        }
        return notificationRepo.findAllByClassIdIdAndAndTituloContaining(classId, titulo, pageable).map(notification -> new NotificationDTO(
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
                        notification.getClassId().getAnoLetivo(),
                        notification.getClassId().getCurso().getNome()
                )
        ));
    }

    public Page<NotificationWithStatusDTO> getAllByClassAndStatus(JwtAuthenticationToken token, long classId, String titulo, Pageable pageable) {
        if (titulo == null || titulo.isBlank()) {
            titulo = "";
        }
        long studentId = studentRepo.findByUserIdId(Long.parseLong(token.getName()))
                .orElseThrow(()-> new EntityNotFoundException("Estudante nao encotrado")).getId();
        return notificationRepo.findAllByClassIdIdAndAndTituloContaining(classId, titulo, pageable)
                .map(notification -> {
                    var statusOpt = noticacaoStatusRepository.findByNotificacaoIdAndStudentId(notification.getId(), studentId);
                    boolean lida = statusOpt.map(NotificacaoStatus::isLida).orElse(false);
                    LocalDateTime dataLeitura = statusOpt.map(NotificacaoStatus::getDataLeitura).orElse(null);

                    return new NotificationWithStatusDTO(
                            notification.getId(),
                            notification.getTitulo(),
                            notification.getDescricao(),
                            notification.getDataCriacao(),
                            lida,
                            dataLeitura,
                            new ClassDTO(
                                    notification.getClassId().getId(),
                                    notification.getClassId().getNome(),
                                    notification.getClassId().getDocente().getName(),
                                    notification.getClassId().getDescricao(),
                                    notification.getClassId().getAno(),
                                    notification.getClassId().getAnoLetivo(),
                                    notification.getClassId().getCurso().getNome()
                            )
                    );
                });
    }

    public void marcarComoLida(JwtAuthenticationToken token, Long notificacaoId){
        Student student = studentRepo.findByUserIdId(Long.parseLong(token.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Estudente nao econtrado"));
        NotificacaoStatus notificacaoStatus = noticacaoStatusRepository.findById(
                        new NotificacaoStatusId(
                                notificacaoId,
                                student.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Notificacao nao encontrada"));
        notificacaoStatus.setLida(true);
        notificacaoStatus.setDataLeitura(LocalDateTime.now());
        noticacaoStatusRepository.save(notificacaoStatus);
    }

    @Transactional
    public int marcarTodasComoLidas(JwtAuthenticationToken token) {
        long studentId = studentRepo.findByUserIdId(Long.parseLong(token.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Estudente nao econtrado")).getId();
        return noticacaoStatusRepository.marcarTodasComoLidas(studentId);
    }

    @Transactional
    public int marcarTodasComoLidasPorTurma(JwtAuthenticationToken token, long classId) {
        long studentId = studentRepo.findByUserIdId(Long.parseLong(token.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Estudente nao econtrado")).getId();
        return noticacaoStatusRepository.marcarTodasComoLidasPorTurma(studentId, classId);
    }

    public long contarNaoLidasTotais(JwtAuthenticationToken token) {
        long studentId = studentRepo.findByUserIdId(Long.parseLong(token.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"))
                .getId();
        return noticacaoStatusRepository.countNaoLidasByStudent(studentId);
    }

    public long contarNaoLidasPorTurma(JwtAuthenticationToken token, long classId) {
        long studentId = studentRepo.findByUserIdId(Long.parseLong(token.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"))
                .getId();

        return noticacaoStatusRepository.countNaoLidasByStudentAndClass(studentId, classId);
    }

    public NotificacaoStatsDTO getEstatisticasPorTurma(long classId) {
        long total = noticacaoStatusRepository.countTotalByClass(classId);
        long lidas = noticacaoStatusRepository.countLidasByClass(classId);
        long naoLidas = total - lidas;

        double percentual = total == 0 ? 0.0 : ((double) lidas / total) * 100.0;

        return new NotificacaoStatsDTO(total, lidas, naoLidas, percentual);
    }

    @Transactional
    public void delete(long id) {
        if (!notificationRepo.existsById(id)) {
            throw new EntityNotFoundException("Notificação não encontrada com ID: " + id);
        }
        notificationRepo.deleteById(id);
    }

}