package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.WorkTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTaskRepo extends JpaRepository<WorkTask, Long> {
    Page<WorkTask> findAllByClassIdId(long classId, Pageable pageable);
}