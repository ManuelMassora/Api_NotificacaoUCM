package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnuncioRepo extends JpaRepository<Anuncio, Long> {
}
