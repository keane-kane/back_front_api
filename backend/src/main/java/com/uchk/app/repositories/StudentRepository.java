package com.uchk.app.repositories;

import com.uchk.app.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByIne(String ine);
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByFormation(String formation);
    
    List<Student> findByPromotion(String promotion);
    
    @Query("SELECT s FROM Student s WHERE s.endYear IS NOT NULL")
    List<Student> findAllGraduates();
    
    @Query("SELECT s FROM Student s WHERE s.endYear = :year")
    List<Student> findByGraduationYear(@Param("year") Integer year);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.employmentStatus = :status")
    long countByEmploymentStatus(@Param("status") String status);
    
    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.ine) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Student> search(@Param("query") String query);
}
