package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.service.CursoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/curso")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<Curso> create(@RequestBody String nome, UriComponentsBuilder uriBuilder) {
        var novoCurso = cursoService.create(nome);
        URI uri = uriBuilder.path("/curso/{id}").buildAndExpand(novoCurso.getId()).toUri();
        return ResponseEntity.created(uri).body(novoCurso);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> findById(@PathVariable Long id) {
        var curso = cursoService.findById(id);
        return ResponseEntity.ok(curso);
    }

    @GetMapping
    public ResponseEntity<Page<Curso>> findAll(Pageable pageable) {
        var cursos = cursoService.findAll(pageable);
        return ResponseEntity.ok(cursos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(@PathVariable Long id, @RequestBody String nome) {
        var cursoAtualizado = cursoService.update(id, nome);
        return ResponseEntity.ok(cursoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
