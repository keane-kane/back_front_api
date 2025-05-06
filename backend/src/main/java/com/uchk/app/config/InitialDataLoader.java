package com.uchk.app.config;

import com.uchk.app.models.Role;
import com.uchk.app.models.User;
import com.uchk.app.repositories.RoleRepository;
import com.uchk.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create roles if they don't exist
        createRoleIfNotFound("ROLE_ADMIN", "Administrator role");
        createRoleIfNotFound("ROLE_INSTRUCTOR", "Instructor role");
        createRoleIfNotFound("ROLE_STUDENT", "Student role");
        createRoleIfNotFound("ROLE_ADMINISTRATIVE", "Administrative staff role");
        
        // Create admin user if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@uchk.edu");
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            
            userRepository.save(admin);
        }
    }
    
    private void createRoleIfNotFound(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}