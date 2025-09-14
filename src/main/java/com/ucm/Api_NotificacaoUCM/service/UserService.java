package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.CreateStudent;
import com.ucm.Api_NotificacaoUCM.dto.CreateUser;
import com.ucm.Api_NotificacaoUCM.model.Role;
import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.model.User;
import com.ucm.Api_NotificacaoUCM.repo.RoleRepo;
import com.ucm.Api_NotificacaoUCM.repo.StudentRepo;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final StudentRepo studentRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, StudentRepo studentRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.studentRepo = studentRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
        if (userRepo.findByEmail(createStudent.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
        var role = roleRepo.findByName(Role.Values.BASIC.name());
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
            studentRepo.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Error saving student data: " + e.getMessage(), e);
        }
    }

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
}
