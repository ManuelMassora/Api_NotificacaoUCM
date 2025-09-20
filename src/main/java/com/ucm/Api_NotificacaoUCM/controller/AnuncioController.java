package com.ucm.Api_NotificacaoUCM.controller;

import com.ucm.Api_NotificacaoUCM.dto.CreateAnuncio;
import com.ucm.Api_NotificacaoUCM.model.Anuncio;
import com.ucm.Api_NotificacaoUCM.service.AnuncioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("anuncio")
public class AnuncioController {
    private final AnuncioService anuncioService;

    public AnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Anuncio> adicionar(@RequestBody @Valid CreateAnuncio dto, UriComponentsBuilder uriBuilder) {
        var anuncio = anuncioService.adicionar(dto);
        var uri = uriBuilder.path("/anuncio/{id}").buildAndExpand(anuncio.getId()).toUri();
        return ResponseEntity.created(uri).body(anuncio);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            anuncioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anuncio> buscar(@PathVariable Long id) {
        try {
            var anuncio = anuncioService.get(id);
            return ResponseEntity.ok(anuncio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Anuncio>> listarTodos(@PageableDefault(size = 10, sort = {"titulo"}) Pageable pageable) {
        var page = anuncioService.getAllFilter(pageable);
        return ResponseEntity.ok(page);
    }

}
