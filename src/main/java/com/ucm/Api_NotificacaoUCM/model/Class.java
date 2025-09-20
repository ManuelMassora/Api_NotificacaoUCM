package com.ucm.Api_NotificacaoUCM.model;

import jakarta.persistence.*;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "Nome", length = 255)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "Docente_ID")
    private User docente;

    @Column(name = "Descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ano")
    private int ano;

    @Column(name = "ano_letivo")
    private Year anoLetivo;

    @ManyToMany(mappedBy = "classes")
    private Set<Student> students = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "Curso_ID")
    private Curso curso;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public User getDocente() {
        return docente;
    }

    public void setDocente(User docente) {
        this.docente = docente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Year getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(Year anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}