package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
