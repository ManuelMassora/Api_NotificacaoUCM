package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.ClassDTO;
import com.ucm.Api_NotificacaoUCM.dto.CreateClass;
import com.ucm.Api_NotificacaoUCM.dto.UpdateClass;
import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")
public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<Void> newClass(@Valid @RequestBody CreateClass createClass, JwtAuthenticationToken token) {
        classService.newClass(createClass, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<Void> deleteClass(@PathVariable("id") Long id, JwtAuthenticationToken token) {
        classService.delete(id, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<Void> patchClass(@PathVariable("id") Long id, @RequestBody UpdateClass updateClass, JwtAuthenticationToken token) {
        classService.patchClass(id, updateClass, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<ClassDTO> findById(@PathVariable("id") Long id) {
        ClassDTO classe = classService.findById(id);
        return ResponseEntity.ok(classe);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('professor')")
    public ResponseEntity<Page<ClassDTO>> listByToken(JwtAuthenticationToken token, Pageable pageable) {
        Page<ClassDTO> classes = classService.listByToken(token, pageable);
        return ResponseEntity.ok(classes);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('professor', 'admin')")
    public ResponseEntity<Page<ClassDTO>> listAll(Pageable pageable) {
        Page<ClassDTO> classes = classService.listAll(pageable);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/cursoid/{id}")
    public ResponseEntity<Page<ClassDTO>> listByCurso(@PathVariable("id") long cursoid, Pageable pageable) {
        Page<ClassDTO> classes = classService.listByCurso(cursoid, pageable);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/estudante")
    public ResponseEntity<Page<ClassDTO>> listByEstudanteToken(JwtAuthenticationToken token,Pageable pageable) {
        Page<ClassDTO> classes = classService.listByStudentToken(token, pageable);
        return ResponseEntity.ok(classes);
    }
}