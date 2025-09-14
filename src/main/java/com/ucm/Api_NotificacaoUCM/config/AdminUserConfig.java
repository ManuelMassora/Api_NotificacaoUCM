package com.ucm.Api_NotificacaoUCM.config;

import com.ucm.Api_NotificacaoUCM.model.Role;
import com.ucm.Api_NotificacaoUCM.model.User;
import com.ucm.Api_NotificacaoUCM.repo.RoleRepo;
import com.ucm.Api_NotificacaoUCM.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(UserRepo userRepository,
                           RoleRepo roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepository;
        this.roleRepo = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepo.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepo.findByEmail("admin123@email.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Ja existe");
                },
                () -> {
                    var user = new User();
                    user.setName("admin123");
                    user.setEmail("admin123@email.com");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepo.save(user);
                }
        );
    }
}
