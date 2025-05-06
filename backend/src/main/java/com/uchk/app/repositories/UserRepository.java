package com.uchk.app.repositories;

import com.uchk.app.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //@EntityGraph(attributePaths = "roles") // optional with EAGER, but safe
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findAllByRoleName(String roleName);
    
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    long countByRoleName(String roleName);
}
