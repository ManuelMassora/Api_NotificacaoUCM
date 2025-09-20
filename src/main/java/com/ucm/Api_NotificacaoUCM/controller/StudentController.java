package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.ClassDTO;
import com.ucm.Api_NotificacaoUCM.dto.StudentDTO;
import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add-class/{classId}")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Void> adicionarClasse(@PathVariable long classId, JwtAuthenticationToken token) {
        try {
            studentService.adicionarClasse(token, classId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{studentId}/add-class/{classId}")
    @PreAuthorize("hasAnyAuthority('admin','professor')")
    public ResponseEntity<Void> adicionarClasse(@PathVariable long studentId, @PathVariable long classId) {
        try {
            studentService.adicionarClasse(studentId, classId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{studentId}/remove-class/{classId}")
    @PreAuthorize("hasAnyAuthority('admin','professor')")
    public ResponseEntity<Void> removerClasse(@PathVariable long studentId, @PathVariable long classId) {
        try {
            studentService.removerClasse(studentId, classId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/classes")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Page<ClassDTO>> listarClassesDoEstudante(JwtAuthenticationToken token, Pageable pageable) {
        try {
            var classes = studentService.listarClassesDoEstudante(token, pageable);
            return ResponseEntity.ok(classes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{studentId}/classes")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Page<ClassDTO>> listarClassesDoEstudante(@PathVariable long studentId, Pageable pageable) {
        try {
            var classes = studentService.listarClassesDoEstudante(studentId, pageable);
            return ResponseEntity.ok(classes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curso")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<Curso> buscarCursoDoEstudante(JwtAuthenticationToken token) {
        try {
            var curso = studentService.buscarCursoDoEstudante(token);
            return ResponseEntity.ok(curso);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{studentId}/curso")
    @PreAuthorize("hasAnyAuthority('admin','professor')")
    public ResponseEntity<Curso> buscarCursoDoEstudante(@PathVariable long studentId) {
        try {
            var curso = studentService.buscarCursoDoEstudante(studentId);
            return ResponseEntity.ok(curso);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/perfil")
    @PreAuthorize("hasAnyAuthority('basic')")
    public ResponseEntity<StudentDTO> MyPerfil(JwtAuthenticationToken token) {
        try {
            var user = studentService.MyPerfil(token);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{studentId}/ano-academico")
    @PreAuthorize("hasAnyAuthority('admin','professor')")
    public ResponseEntity<Void> atualizarAnoAcademico(@PathVariable Long studentId, @RequestBody int novoAno) {
        try {
            studentService.atualizarAnoAcademico(studentId, novoAno);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
