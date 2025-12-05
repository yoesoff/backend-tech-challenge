package com.mhyusuf.auth.runner;

import com.mhyusuf.auth.entity.Role;
import com.mhyusuf.auth.entity.User;
import com.mhyusuf.auth.repository.RoleRepository;
import com.mhyusuf.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they do not exist
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("USER");
        createRoleIfNotExists("MODERATOR");

        // Create users with email if they do not exist
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role userRole = roleRepository.findByName("USER").orElseThrow();
        Role moderatorRole = roleRepository.findByName("MODERATOR").orElseThrow();

        createUserIfNotExists("admin@example.com", "admin", "password123", List.of(adminRole));
        createUserIfNotExists("user1@example.com", "user1", "password123", List.of(userRole));
        createUserIfNotExists("user2@example.com", "user2", "password123", List.of(userRole));
        createUserIfNotExists("moderator@example.com", "moderator", "password123", List.of(moderatorRole));
        createUserIfNotExists("superadmin@example.com", "superadmin", "password123", List.of(adminRole, moderatorRole));
    }

    private void createRoleIfNotExists(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            roleRepository.save(new Role(roleName));
        }
    }

    private void createUserIfNotExists(String email, String username, String password, List<Role> roles) {
        if (!userRepository.existsByEmail(email) && !userRepository.existsByUsername(username)) {
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(new java.util.HashSet<>(roles));
            userRepository.save(user);
        }
    }
}