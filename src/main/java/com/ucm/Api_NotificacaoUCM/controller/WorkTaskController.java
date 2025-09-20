package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.CreateWorkTask;
import com.ucm.Api_NotificacaoUCM.dto.WorkTaskDTO;
import com.ucm.Api_NotificacaoUCM.model.WorkTask;
import com.ucm.Api_NotificacaoUCM.service.WorkTaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.Optional;

@RestController
@RequestMapping("task")
public class WorkTaskController {
    private final WorkTaskService workTaskService;

    public WorkTaskController(WorkTaskService workTaskService) {
        this.workTaskService = workTaskService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<WorkTaskDTO> create(@RequestBody @Valid CreateWorkTask dto) {
        var createdTask = workTaskService.create(dto);
        return ResponseEntity.ok().body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkTaskDTO> getById(@PathVariable long id) {
        WorkTaskDTO workTask = workTaskService.getById(id);
        return ResponseEntity.ok(workTask);
    }

    @GetMapping
    public ResponseEntity<Page<WorkTaskDTO>> getAll(@PageableDefault(size = 10, sort = {"titulo"}) Pageable pageable) {
        return ResponseEntity.ok(workTaskService.getAll(pageable));
    }

    @GetMapping("/by-class/{classId}")
    public ResponseEntity<Page<WorkTaskDTO>> getAllByClass(@PathVariable long classId, @PageableDefault(size = 10, sort = {"dataEntrega"}) Pageable pageable) {
        try {
            Page<WorkTaskDTO> tasks = workTaskService.getAllByClass(classId, pageable);
            return ResponseEntity.ok(tasks);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<WorkTaskDTO> update(@PathVariable long id, @RequestBody @Valid CreateWorkTask dto) {
        try {
            var updatedTask = workTaskService.update(id, dto);
            return ResponseEntity.ok(updatedTask);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        try {
            workTaskService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
