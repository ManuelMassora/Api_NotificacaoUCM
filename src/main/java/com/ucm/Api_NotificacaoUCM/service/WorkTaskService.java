package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.ClassDTO;
import com.ucm.Api_NotificacaoUCM.dto.CreateWorkTask;
import com.ucm.Api_NotificacaoUCM.dto.WorkTaskDTO;
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
    public WorkTaskDTO create(CreateWorkTask dto) {
        var classe = classRepo.findById(dto.classId())
                .orElseThrow(() -> new EntityNotFoundException("Class nao encontrada com ID: " + dto.classId()));

        var workTask = new WorkTask();
        workTask.setTitulo(dto.titulo());
        workTask.setDescricao(dto.descricao());
        workTask.setDataEntrega(dto.dataEntrega());
        workTask.setClassId(classe);
        workTask.setDataCriacao(LocalDateTime.now());

        var workTaskSave = workTaskRepo.save(workTask);

        return new WorkTaskDTO(
                workTaskSave.getId(),
                workTaskSave.getTitulo(),
                workTaskSave.getDescricao(),
                workTaskSave.getDataEntrega(),
                workTaskSave.getDataCriacao(),
                new ClassDTO(
                        workTaskSave.getClassId().getId(),
                        workTaskSave.getClassId().getNome(),
                        workTaskSave.getClassId().getDocente().getName(),
                        workTaskSave.getClassId().getDescricao(),
                        workTaskSave.getClassId().getAno(),
                        workTaskSave.getClassId().getAnoLetivo()
                )
        );
    }

    public WorkTaskDTO getById(long id) {
        var workTask = workTaskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        var classDto = new ClassDTO(
                workTask.getClassId().getId(),
                workTask.getClassId().getNome(),
                workTask.getClassId().getDocente().getName(),
                workTask.getClassId().getDescricao(),
                workTask.getClassId().getAno(),
                workTask.getClassId().getAnoLetivo()
        );
        return new WorkTaskDTO(
                workTask.getId(),
                workTask.getTitulo(),
                workTask.getDescricao(),
                workTask.getDataEntrega(),
                workTask.getDataCriacao(),
                classDto
        );
    }

    public Page<WorkTaskDTO> getAll(Pageable pageable) {
        return workTaskRepo.findAll(pageable).map(workTask -> new WorkTaskDTO(
                workTask.getId(),
                workTask.getTitulo(),
                workTask.getDescricao(),
                workTask.getDataEntrega(),
                workTask.getDataCriacao(),
                new ClassDTO(
                        workTask.getClassId().getId(),
                        workTask.getClassId().getNome(),
                        workTask.getClassId().getDocente().getName(),
                        workTask.getClassId().getDescricao(),
                        workTask.getClassId().getAno(),
                        workTask.getClassId().getAnoLetivo()
                )
        ));
    }

    public Page<WorkTaskDTO> getAllByClass(long classId, Pageable pageable) {
        return workTaskRepo.findAllByClassIdId(classId,pageable).map(workTask -> new WorkTaskDTO(
                workTask.getId(),
                workTask.getTitulo(),
                workTask.getDescricao(),
                workTask.getDataEntrega(),
                workTask.getDataCriacao(),
                new ClassDTO(
                        workTask.getClassId().getId(),
                        workTask.getClassId().getNome(),
                        workTask.getClassId().getDocente().getName(),
                        workTask.getClassId().getDescricao(),
                        workTask.getClassId().getAno(),
                        workTask.getClassId().getAnoLetivo()
                )
        ));
    }

    @Transactional
    public WorkTaskDTO update(long id, CreateWorkTask dto) {
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
        var workTaskSave = workTaskRepo.save(workTask);

        return new WorkTaskDTO(
                workTaskSave.getId(),
                workTaskSave.getTitulo(),
                workTaskSave.getDescricao(),
                workTaskSave.getDataEntrega(),
                workTaskSave.getDataCriacao(),
                new ClassDTO(
                        workTaskSave.getClassId().getId(),
                        workTaskSave.getClassId().getNome(),
                        workTaskSave.getClassId().getDocente().getName(),
                        workTaskSave.getClassId().getDescricao(),
                        workTaskSave.getClassId().getAno(),
                        workTaskSave.getClassId().getAnoLetivo()
                )
        );
    }

    @Transactional
    public void delete(long id) {
        var workTask = workTaskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkTask nao encontrada com ID: " + id));
        workTaskRepo.delete(workTask);
    }
}