package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.ClassDTO;
import com.ucm.Api_NotificacaoUCM.dto.StudentDTO;
import com.ucm.Api_NotificacaoUCM.dto.UserDTO;
import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.repo.ClassRepo;
import com.ucm.Api_NotificacaoUCM.repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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

    public Page<ClassDTO> listarClassesDoEstudante(JwtAuthenticationToken token, Pageable pageable) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante nao existe"));
        Page<Class> classesPage = classRepo.findByStudents_Id(student.getId(), pageable);
        var classDtos = classesPage.map(classe -> new ClassDTO(
                classe.getId(),
                classe.getNome(),
                classe.getDocente().getName(),
                classe.getDescricao(),
                classe.getAno(),
                classe.getAnoLetivo(),
                classe.getCurso().getNome()
        ));
        return classDtos;
    }

    public Page<ClassDTO> listarClassesDoEstudante(long studentId, Pageable pageable) {
        var student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        Page<Class> classesPage = classRepo.findByStudents_Id(student.getId(), pageable);
        var classDtos = classesPage.map(classe -> new ClassDTO(
                classe.getId(),
                classe.getNome(),
                classe.getDocente().getName(),
                classe.getDescricao(),
                classe.getAno(),
                classe.getAnoLetivo(),
                classe.getCurso().getNome()
        ));
        return classDtos;
    }

    public Curso buscarCursoDoEstudante(JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        return student.getCurso();
    }

    public StudentDTO MyPerfil(JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        var student = studentRepo.findByUserIdId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        var userDTO = new UserDTO(
                student.getUserId().getId(),
                student.getUserId().getName(),
                student.getUserId().getEmail()
        );
        var studentDTO = new StudentDTO(
                userDTO,
                student.getId(),
                student.getStudentNumber(),
                student.getAnoAcademindo(),
                student.getCurso()
        );
        return studentDTO;
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
