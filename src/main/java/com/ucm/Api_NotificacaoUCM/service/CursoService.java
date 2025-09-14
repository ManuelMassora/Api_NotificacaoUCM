package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.model.Curso;
import com.ucm.Api_NotificacaoUCM.repo.CursoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso create(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do curso não pode ser nulo ou vazio.");
        }

        var curso = new Curso();
        curso.setNome(nome);

        try {
            return cursoRepository.save(curso);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o curso: " + e.getMessage());
        }
    }

    public Curso findById(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado com o ID: " + id));
    }

    public Page<Curso> findAll(Pageable pageable) {
        return cursoRepository.findAll(pageable);
    }

    public Curso update(Long id, String nome) {
        var curso = findById(id);
        curso.setNome(nome);

        try {
            return cursoRepository.save(curso);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o curso: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        var curso = cursoRepository.findById(id);
        try {
            cursoRepository.delete(curso.get());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar o curso: " + e.getMessage());
        }
    }
}