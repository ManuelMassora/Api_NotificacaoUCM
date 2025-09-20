package com.ucm.Api_NotificacaoUCM.service;

import com.ucm.Api_NotificacaoUCM.dto.CreateAnuncio;
import com.ucm.Api_NotificacaoUCM.model.Anuncio;
import com.ucm.Api_NotificacaoUCM.repo.AnuncioRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnuncioService {
    private final AnuncioRepo anuncioRepo;

    public AnuncioService(AnuncioRepo anuncioRepo) {
        this.anuncioRepo = anuncioRepo;
    }

    @Transactional
    public Anuncio adicionar(CreateAnuncio dto) {
        var anuncio = new Anuncio();
        anuncio.setTitulo(dto.titulo());
        anuncio.setDescricao(dto.descricao());
        anuncio.setDataCriacao(LocalDateTime.now());
        return anuncioRepo.save(anuncio);
    }

    @Transactional
    public void delete(long id) {
        var anuncio = anuncioRepo.findById(id);
        if (anuncio.isEmpty()) {
            throw new EntityNotFoundException("Anuncio nao econtrado");
        }
        anuncioRepo.delete(anuncio.get());
    }

    public Anuncio get(long id) {
        var anuncio = anuncioRepo.findById(id);
        if (anuncio.isEmpty()) {
            throw new EntityNotFoundException("Anuncio nao econtrado");
        }
        return anuncio.get();
    }

    public Page<Anuncio> getAllFilter(Pageable pageable) {
        return anuncioRepo.findAll(pageable);
    }
}