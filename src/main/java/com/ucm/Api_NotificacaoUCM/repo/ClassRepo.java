package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassRepo extends JpaRepository<Class, Long> {
    Page<Class> findByDocente(User docente, Pageable pageable);
    Page<Class> findByCurso(Curso curso, Pageable pageable);
    Page<Class> findByCursoAndAnoLessThanEqualAndNomeContaining(Curso curso, int ano, String nome, Pageable pageable);
    @Query("SELECT c FROM Class c JOIN c.students s WHERE s.id = :studentId")
    Page<Class> findByStudents_Id(@Param("studentId") Long studentId, Pageable pageable);
}