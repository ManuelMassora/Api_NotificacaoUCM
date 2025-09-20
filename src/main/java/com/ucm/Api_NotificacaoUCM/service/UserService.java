package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.CreateStudent;
import com.ucm.Api_NotificacaoUCM.dto.CreateUser;
import com.ucm.Api_NotificacaoUCM.model.Role;
import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.model.User;
import com.ucm.Api_NotificacaoUCM.repo.CursoRepository;
import com.ucm.Api_NotificacaoUCM.repo.RoleRepo;
import com.ucm.Api_NotificacaoUCM.repo.StudentRepo;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final StudentRepo studentRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CursoRepository cursoRepository;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, StudentRepo studentRepo, BCryptPasswordEncoder bCryptPasswordEncoder,
                       CursoRepository cursoRepository) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.studentRepo = studentRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public void CreateStudent(CreateStudent createStudent) {
        if (createStudent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreateStudent object cannot be null");
        }
        if (createStudent.email() == null || createStudent.email().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be null or empty");
        }
        if (createStudent.nome() == null || createStudent.nome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be null or empty");
        }
        if (createStudent.senha() == null || createStudent.senha().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty");
        }
        if (createStudent.studentNumber() == null || createStudent.studentNumber().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student number cannot be null or empty");
        }
        if (createStudent.studentNumber() == null || createStudent.studentNumber().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student number cannot be null or empty");
        }
        if (createStudent.cursoid() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CursoID cannot be null or empty");
        }
        if (createStudent.anoacademico() == 0 || createStudent.anoacademico() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ANo nao pode ser vazio, e maior que 5");
        }
        if (userRepo.findByEmail(createStudent.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
        var role = roleRepo.findByName(Role.Values.BASIC.name());
        var curso = cursoRepository.findById(createStudent.cursoid());
        if (curso.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso ID nao existe");
        }
        var user = new User();
        user.setEmail(createStudent.email());
        user.setName(createStudent.nome());
        user.setPassword(bCryptPasswordEncoder.encode(createStudent.senha()));
        user.setRoles(Collections.singleton(role));

        try {
            var usersafe = userRepo.save(user);
            var student = new Student();
            student.setUserId(usersafe);
            student.setStudentNumber(createStudent.studentNumber());
            student.setCurso(curso.get());
            student.setAnoAcademindo(createStudent.anoacademico());
            studentRepo.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Error saving student data: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void CreateTeacher(CreateUser createUser) {
        var role = roleRepo.findByName(Role.Values.PROFESSOR.name());
        if (createUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreateStudent object cannot be null");
        }
        if (createUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreateStudent object cannot be null");
        }
        if (createUser.email() == null || createUser.email().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be null or empty");
        }
        if (createUser.nome() == null || createUser.nome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be null or empty");
        }
        if (createUser.senha() == null || createUser.senha().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty");
        }
        if (userRepo.findByEmail(createUser.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
        var user = new User();
        user.setEmail(createUser.email());
        user.setName(createUser.nome());
        user.setPassword(bCryptPasswordEncoder.encode(createUser.senha()));
        user.setRoles(Collections.singleton(role));

        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving student data: " + e.getMessage(), e);
        }
    }

    public User getUser(long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrada com ID: " + id));
    }

    public User MyProfile(JwtAuthenticationToken token) {
        return userRepo.findById(Long.parseLong(token.getName())).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrada com ID"));
    }

    public Page<User> getAllUser(Pageable pageable) {
        return userRepo.findAll(pageable);
    }
}
