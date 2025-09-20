package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.repo.ClassRepo;
import com.ucm.Api_NotificacaoUCM.repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StudentService {
    private final StudentRepo studentRepo;
    private final ClassRepo classRepo;

    public StudentService(StudentRepo studentRepo, ClassRepo classRepo) {
        this.studentRepo = studentRepo;
        this.classRepo = classRepo;
    }

    @Transactional
    public void adicionarClasse(JwtAuthenticationToken token, long classId) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        var classe = classRepo.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Classe não encontrada com ID: " + classId));

        if (!student.getClasses().contains(classe)) {
            student.getClasses().add(classe);
            studentRepo.save(student);
        }
    }

    @Transactional
    public void adicionarClasse(long studentId, long classId) {
        var student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        var classe = classRepo.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Classe não encontrada com ID: " + classId));
        student.getClasses().add(classe);
        studentRepo.save(student);
    }

    @Transactional
    public void removerClasse(long studentId, long classId) {
        var student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        var classe = classRepo.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Classe não encontrada com ID: " + classId));
        student.getClasses().remove(classe);
        studentRepo.save(student);
    }

    public Set<Class> listarClassesDoEstudante(JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        return student.getClasses();
    }

    public Set<Class> listarClassesDoEstudante(long studentId) {
        var student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        return student.getClasses();
    }

    public Curso buscarCursoDoEstudante(JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        return student.getCurso();
    }

    public Student MyPerfil(JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        return student;
    }

    public Curso buscarCursoDoEstudante(long studentId) {
        var student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        return student.getCurso();
    }

    @Transactional
    public void atualizarAnoAcademico(Long studentId, int novoAno) {
        var student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        student.setAnoAcademindo(novoAno);
    }
}
