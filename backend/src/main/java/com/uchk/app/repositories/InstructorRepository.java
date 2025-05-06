package com.uchk.app.repositories;

import com.uchk.app.models.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    
    Optional<Instructor> findByUserId(Long userId);
    
    Optional<Instructor> findByEmail(String email);
    
    List<Instructor> findByType(Instructor.InstructorType type);
    
    @Query("SELECT i FROM Instructor i WHERE LOWER(i.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.specialization) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Instructor> search(@Param("query") String query);
}
