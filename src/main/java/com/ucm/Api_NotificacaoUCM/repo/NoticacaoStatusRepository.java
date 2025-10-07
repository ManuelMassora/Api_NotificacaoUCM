package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.NotificacaoStatus;
import com.ucm.Api_NotificacaoUCM.model.NotificacaoStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticacaoStatusRepository extends JpaRepository<NotificacaoStatus, NotificacaoStatusId> {
    Optional<NotificacaoStatus> findByNotificacaoIdAndStudentId(long notificacaoId, long studentId);
    @Query("SELECT COUNT(ns) FROM NotificacaoStatus ns " +
            "WHERE ns.student.id = :studentId AND ns.lida = false")
    long countNaoLidasByStudent(@Param("studentId") Long studentId);
    @Query("SELECT COUNT(ns) FROM NotificacaoStatus ns " +
            "WHERE ns.student.id = :studentId AND ns.lida = false " +
            "AND ns.notificacao.classId.id = :classId")
    long countNaoLidasByStudentAndClass(@Param("studentId") Long studentId, @Param("classId") Long classId);
    @Query("SELECT COUNT(ns) FROM NotificacaoStatus ns WHERE ns.notificacao.classId.id = :classId")
    long countTotalByClass(@Param("classId") Long classId);

    @Query("SELECT COUNT(ns) FROM NotificacaoStatus ns WHERE ns.notificacao.classId.id = :classId AND ns.lida = true")
    long countLidasByClass(@Param("classId") Long classId);
    @Modifying
    @Query("UPDATE NotificacaoStatus ns SET ns.lida = true, ns.dataLeitura = CURRENT_TIMESTAMP " +
            "WHERE ns.student.id = :studentId AND ns.lida = false")
    int marcarTodasComoLidas(@Param("studentId") long studentId);

    @Modifying
    @Query("UPDATE NotificacaoStatus ns SET ns.lida = true, ns.dataLeitura = CURRENT_TIMESTAMP " +
            "WHERE ns.student.id = :studentId AND ns.notificacao.classId.id = :classId AND ns.lida = false")
    int marcarTodasComoLidasPorTurma(@Param("studentId") long studentId, @Param("classId") long classId);
}