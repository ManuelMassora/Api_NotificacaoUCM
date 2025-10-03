package com.ucm.Api_NotificacaoUCM.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "User_ID")
    private User userId;

    @Column(name = "Student_Number", length = 255)
    private String studentNumber;

    @Column(name = "Ano_Academico")
    @Size(max = 5)
    private int anoAcademindo;

    @ManyToMany
    @JoinTable(
            name = "studentclass",
            joinColumns = @JoinColumn(name = "Estudante_ID"),
            inverseJoinColumns = @JoinColumn(name = "Class_ID")
    )
    private Set<Class> classes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "Curso_ID")
    private Curso curso;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getAnoAcademindo() {
        return anoAcademindo;
    }

    public void setAnoAcademindo(int anoAcademindo) {
        this.anoAcademindo = anoAcademindo;
    }
}