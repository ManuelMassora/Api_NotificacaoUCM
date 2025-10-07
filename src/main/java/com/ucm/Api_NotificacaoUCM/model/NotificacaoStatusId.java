package com.ucm.Api_NotificacaoUCM.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
public class NotificacaoStatusId implements Serializable {
    @Column(name = "notificacao_id")
    private Long notificacaoId;
    @Column(name = "student_id")
    private Long studentId;
    public NotificacaoStatusId() {}
    public NotificacaoStatusId(Long notificacaoId, Long studentId) {
        this.notificacaoId = notificacaoId;
        this.studentId = studentId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificacaoStatusId)) return false;
        NotificacaoStatusId that = (NotificacaoStatusId) o;
        return Objects.equals(notificacaoId, that.notificacaoId) &&
                Objects.equals(studentId, that.studentId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(notificacaoId, studentId);
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
}
