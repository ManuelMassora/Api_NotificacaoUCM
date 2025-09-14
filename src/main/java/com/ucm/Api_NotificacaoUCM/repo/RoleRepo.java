package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
