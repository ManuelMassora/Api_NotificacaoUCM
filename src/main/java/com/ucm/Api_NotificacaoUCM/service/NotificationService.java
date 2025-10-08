package com.ucm.Api_NotificacaoUCM.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucm.Api_NotificacaoUCM.config.RabbitMQConfig;
import com.ucm.Api_NotificacaoUCM.dto.*;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final ClassRepo classRepo;
    private final StudentRepo studentRepo;
    private final NoticacaoStatusRepository noticacaoStatusRepository;
    private final RabbitTemplate rabbitTemplate;

    public NotificationService(NotificationRepo notificationRepo,
                               ClassRepo classRepo,
                               StudentRepo studentRepo,
                               NoticacaoStatusRepository noticacaoStatusRepository,
                               RabbitTemplate rabbitTemplate) {
        this.notificationRepo = notificationRepo;
        this.classRepo = classRepo;
        this.studentRepo = studentRepo;
        this.noticacaoStatusRepository = noticacaoStatusRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public NotificationDTO create(CreateNotification dto) throws JsonProcessingException {
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
        var notificationMessage = new NotificationMessage(notificationSave.getId(), notificationSave.getTitulo(), notificationSave.getDescricao(), notificationSave.getClassId().getId());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(notificationMessage);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME, // troca (exchange)
                RabbitMQConfig.ROUTING_KEY,   // chave de roteamento
                json                          // corpo da mensagem
        );
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