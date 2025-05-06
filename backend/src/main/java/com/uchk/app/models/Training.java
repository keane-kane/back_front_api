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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;
    
    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private String location;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Integer maxStudents;
    
    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Entity
    @Table(name = "formations")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EntityListeners(AuditingEntityListener.class)
    public static class Formation {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false, unique = true)
        private String name;
        
        @Column(nullable = false)
        private String level;
        
        @Column(nullable = false)
        private FormationType type;
        
        @Column
        private Double cost;
        
        @Column
        private String fundingType;
        
        @OneToMany(mappedBy = "formation")
        private List<Training> courses = new ArrayList<>();
        
        @CreatedDate
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;
        
        @LastModifiedDate
        @Column(nullable = false)
        private LocalDateTime updatedAt;
        
        public enum FormationType {
            DEGREE,     // Diplôme
            CERTIFICATE,// Certification
            TRAINING,   // Formation
            OTHER       // Autre
        }
    }
}
