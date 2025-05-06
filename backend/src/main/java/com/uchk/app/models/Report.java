package com.uchk.app.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType type;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(length = 5000, nullable = false)
    private String content;
    
    @Column(length = 500)
    private String participants;
    
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum ReportType {
        MEETING,      // Réunion
        ENCOUNTER,    // Rencontre
        SEMINAR,      // Séminaire
        WEBINAR,      // Webinaire
        COUNCIL       // Conseil d'Université
    }
}
