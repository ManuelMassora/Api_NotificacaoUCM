package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepo extends JpaRepository<Class, Long> {
    Page<Class> findByDocente(User docente, Pageable pageable);
    Page<Class> findByCurso(Curso curso, Pageable pageable);
    Page<Class> findByCursoAndAno(Curso curso, int ano, Pageable pageable);
}