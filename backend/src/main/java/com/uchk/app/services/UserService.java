package com.uchk.app.services;

import com.uchk.app.dto.UserDto;
import com.uchk.app.models.Role;
import com.uchk.app.models.User;
import com.uchk.app.repositories.RoleRepository;
import com.uchk.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        System.out.println("Incoming request: " + user.getRoles());
        Set<Role> rolesCopy = new HashSet<>(user.getRoles());
        Set<GrantedAuthority> authorities = rolesCopy.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true, // account non-expired
                true, // credentials non-expired
                true, // account non-locked
                authorities
        );
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String roleName) {
        return userRepository.findAllByRoleName(roleName);
    }

    @Transactional
    public User createUser(User user, Set<String> roleNames) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set roles
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        
        user.setRoles(roles);
        
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // Convert User to UserDto for security reasons (don't expose password, etc.)
    public UserDto convertToDto(User user) {
        if (user == null) return null;
        
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
