package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.CreateWorkTask;
import com.ucm.Api_NotificacaoUCM.model.WorkTask;
import com.ucm.Api_NotificacaoUCM.repo.ClassRepo;
import com.ucm.Api_NotificacaoUCM.repo.WorkTaskRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkTaskService {
    private final WorkTaskRepo workTaskRepo;
    private final ClassRepo classRepo;

    public WorkTaskService(WorkTaskRepo workTaskRepo, ClassRepo classRepo) {
        this.workTaskRepo = workTaskRepo;
        this.classRepo = classRepo;
    }

    @Transactional
    public WorkTask create(CreateWorkTask dto) {
        var classe = classRepo.findById(dto.classId())
                .orElseThrow(() -> new EntityNotFoundException("Class nao encontrada com ID: " + dto.classId()));

        var workTask = new WorkTask();
        workTask.setTitulo(dto.titulo());
        workTask.setDescricao(dto.descricao());
        workTask.setDataEntrega(dto.dataEntrega());
        workTask.setClassId(classe);
        workTask.setDataCriacao(LocalDateTime.now());

        return workTaskRepo.save(workTask);
    }

    public Optional<WorkTask> getById(long id) {
        return workTaskRepo.findById(id);
    }

    public Page<WorkTask> getAll(Pageable pageable) {
        return workTaskRepo.findAll(pageable);
    }

    public Page<WorkTask> getAllByClass(long classId, Pageable pageable) {
        return workTaskRepo.findAllByClassIdId(classId,pageable);
    }

    @Transactional
    public WorkTask update(long id, CreateWorkTask dto) {
        var workTask = workTaskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkTask nao encontrada com ID: " + id));

        if (dto.titulo() != null) {
            workTask.setTitulo(dto.titulo());
        }
        if (dto.descricao() != null) {
            workTask.setDescricao(dto.descricao());
        }
        if (dto.dataEntrega() != null) {
            workTask.setDataEntrega(dto.dataEntrega());
        }

        return workTaskRepo.save(workTask);
    }

    @Transactional
    public void delete(long id) {
        var workTask = workTaskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkTask nao encontrada com ID: " + id));
        workTaskRepo.delete(workTask);
    }
}