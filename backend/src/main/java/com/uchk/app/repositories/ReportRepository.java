package com.uchk.app.repositories;

import com.uchk.app.models.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByType(Report.ReportType type);
    
    List<Report> findByCreatedById(Long userId);
    
    @Query("SELECT r FROM Report r WHERE r.date >= :startDate ORDER BY r.date DESC")
    List<Report> findRecentReports(@Param("startDate") LocalDate startDate);
    
    @Query("SELECT r FROM Report r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Report> search(@Param("query") String query, Pageable pageable);
}
