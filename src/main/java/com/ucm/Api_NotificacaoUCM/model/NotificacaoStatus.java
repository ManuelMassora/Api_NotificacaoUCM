package com.ucm.Api_NotificacaoUCM.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "notificacao_status")
@IdClass(NotificacaoStatusId.class)
public class NotificacaoStatus {
    @Id
    @Column(name = "notificacao_id")
    private Long notificacaoId;

    @Id
    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificacao_id", insertable = false, updatable = false)
    private Notification notificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    private boolean lida = false;
    private LocalDateTime dataLeitura;

    public Notification getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Notification notificacao) {
        this.notificacao = notificacao;
        this.notificacaoId = notificacao.getId();
    }

    public Student getStudent() {
        return student;
    }

    public Long getNotificacaoId() {
        return notificacaoId;
    }

    public void setNotificacaoId(Long notificacaoId) {
        this.notificacaoId = notificacaoId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student.getId();
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(LocalDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }
}