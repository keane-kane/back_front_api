package com.uchk.app.repositories;

import com.uchk.app.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByType(Document.DocumentType type);
    
    List<Document> findByCreatedById(Long userId);
    
    @Query("SELECT d FROM Document d JOIN d.visibleTo v WHERE v = :role")
    List<Document> findByVisibleToRole(@Param("role") String role);
    
    @Query("SELECT COUNT(d) FROM Document d")
    long countDocuments();
    
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.reference) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Document> search(@Param("query") String query);
}
