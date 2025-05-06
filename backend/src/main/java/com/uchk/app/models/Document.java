package com.uchk.app.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    
    @Column
    private String reference;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 10000, nullable = false)
    private String content;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "document_visible_to", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "role_name")
    private Set<String> visibleTo = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum DocumentType {
        MAIL_IN,         // Courrier Arrivé
        MAIL_OUT,        // Courrier Départ
        INTERNAL_NOTE,   // Note de Service Interne
        EXTERNAL_NOTE,   // Note de Service Externe
        ADMIN_NOTE,      // Note Administrative
        CIRCULAR,        // Circulaire
        BUDGET_PLAN,     // Projet de Budget
        BUDGET_REPORT,   // Budget Réalisé
        PERSONNEL_FILE,  // Dossier de Personnel
        STUDENT_FILE     // Dossier d'Étudiant
    }
}
