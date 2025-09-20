package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/curso")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Curso> create(@RequestBody String nome) {
        var novoCurso = cursoService.create(nome);
        return ResponseEntity.ok().body(novoCurso);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> findById(@PathVariable Long id) {
        var curso = cursoService.findById(id);
        return ResponseEntity.ok(curso);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> findAll() {
        var cursos = cursoService.findAll();
        return ResponseEntity.ok(cursos);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(@PathVariable Long id, @RequestBody String nome) {
        var cursoAtualizado = cursoService.update(id, nome);
        return ResponseEntity.ok(cursoAtualizado);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
