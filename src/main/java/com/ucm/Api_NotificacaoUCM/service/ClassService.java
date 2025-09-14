package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.CreateClass;
import com.ucm.Api_NotificacaoUCM.dto.UpdateClass;
import com.ucm.Api_NotificacaoUCM.model.Class;
import com.ucm.Api_NotificacaoUCM.repo.ClassRepo;
import com.ucm.Api_NotificacaoUCM.repo.CursoRepository;
import com.ucm.Api_NotificacaoUCM.repo.StudentRepo;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClassService {
    private final ClassRepo classRepo;
    private final UserRepo userRepo;
    private final CursoRepository cursoRepository;
    private final StudentRepo studentRepo;

    public ClassService(ClassRepo classRepo, UserRepo userRepo, CursoRepository cursoRepository, StudentRepo studentRepo) {
        this.classRepo = classRepo;
        this.userRepo = userRepo;
        this.cursoRepository = cursoRepository;
        this.studentRepo = studentRepo;
    }

    public void newClass(CreateClass createClass, JwtAuthenticationToken token) {
        var user = userRepo.findById(Long.parseLong(token.getName()));
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found");
        }
        if (createClass == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreateClass object cannot be null");
        }
        if (createClass.nome() == null || createClass.nome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome cannot be null or empty");
        }
        if (createClass.ano() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ano cannot be null or empty");
        }
        if (createClass.cursoid() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CursoID cannot be null or empty");
        }
        var curso = cursoRepository.findById(createClass.cursoid());

        var classe = new Class();
        classe.setNome(createClass.nome());
        classe.setDescricao(createClass.descricao());
        classe.setAno(createClass.ano());
        classe.setAnoLetivo(createClass.anoletivo());
        classe.setCurso(curso.get());
        classe.setDocente(user.get());

        try {
            classRepo.save(classe);
        } catch (Exception e) {
            throw new RuntimeException("Error saving Classe data: " + e.getMessage(), e);
        }
    }

    public void delete(long id, JwtAuthenticationToken token) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Whithout classe id in parameter");
        }
        var user = userRepo.findById(Long.parseLong(token.getName()));
        var classe = classRepo.findById(id);
        if (classe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Classe not found");
        }
        if (classe.get().getDocente().getId() != user.get().getId()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "You dont have permission to delete this class");
        }
        try {
            classRepo.delete(classe.get());
        } catch (Exception e) {
            throw new RuntimeException("Error Delecting Classe data: " + e.getMessage(), e);
        }
    }

    public void patchClass(long id, UpdateClass updateClass, JwtAuthenticationToken token) {
        var user = userRepo.findById(Long.parseLong(token.getName()));
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found");
        }

        var classeOptional = classRepo.findById(id);
        if (classeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe not found");
        }

        var classe = classeOptional.get();

        if (classe.getDocente().getId() != user.get().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this class");
        }

        if (updateClass.nome() != null && !updateClass.nome().trim().isEmpty()) {
            classe.setNome(updateClass.nome());
        }
        if (updateClass.descricao() != null) {
            classe.setDescricao(updateClass.descricao());
        }
        if (updateClass.ano() != 0) {
            classe.setAno(updateClass.ano());
        }
        if (updateClass.anoLetivo() != 0) {
            classe.setAnoLetivo(updateClass.anoLetivo());
        }

        try {
            classRepo.save(classe);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Classe data: " + e.getMessage(), e);
        }
    }

    public Class findById(long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid class ID");
        }
        var classe = classRepo.findById(id);
        if (classe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe not found");
        }
        return classe.get();
    }

    public Page<Class> listByToken(JwtAuthenticationToken token, Pageable pageable) {
        var user = userRepo.findById(Long.parseLong(token.getName()));
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found");
        }
        return classRepo.findByDocente(user.get(), pageable);
    }

    public Page<Class> listByCurso(long cursoid, Pageable pageable) {
        var curso = cursoRepository.findById(cursoid);
        if (curso.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Curso not found");
        }
        return classRepo.findByCurso(curso.get(), pageable);
    }

    public Page<Class> listByStudentToken(JwtAuthenticationToken token, Pageable pageable) {
        var user = userRepo.findById(Long.parseLong(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));
        var student = studentRepo.findByUserId(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. Apenas estudantes podem listar classes por curso."));
        var curso = cursoRepository.findById(student.getCurso().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado para o estudante."));
        return classRepo.findByCursoAndAno(curso, student.getAnoAcademindo(), pageable);
    }

    public Page<Class> listAll(Pageable pageable) {
        return classRepo.findAll(pageable);
    }
}
