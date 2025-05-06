package com.uchk.app.repositories;

import com.uchk.app.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    
    @Query("SELECT t FROM Training t WHERE t.formation.id = :formationId")
    List<Training> findByFormationId(@Param("formationId") Long formationId);
    
    @Query("SELECT t FROM Training t WHERE t.instructor.id = :instructorId")
    List<Training> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT t FROM Training t JOIN t.students s WHERE s.id = :studentId")
    List<Training> findByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT t FROM Training t WHERE t.startDate >= :date ORDER BY t.startDate")
    List<Training> findUpcomingCourses(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Training t WHERE t.instructor.id = :instructorId AND t.startDate >= :date ORDER BY t.startDate")
    List<Training> findUpcomingCoursesByInstructor(@Param("instructorId") Long instructorId, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(t) FROM Training t WHERE t.endDate >= CURRENT_DATE")
    long countActiveCourses();
}
