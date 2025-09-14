package com.ucm.Api_NotificacaoUCM.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "worktask")
public class WorkTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "Titulo", length = 255)
    private String titulo;

    @Column(name = "Descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "Data_Entrada")
    private Date dataEntrada;

    @ManyToOne
    @JoinColumn(name = "Class_ID")
    private Class classId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Class getClassId() {
        return classId;
    }

    public void setClassId(Class classId) {
        this.classId = classId;
    }
}