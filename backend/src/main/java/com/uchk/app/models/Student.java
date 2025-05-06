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
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String ine; // Identifiant National Étudiant
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    @Column(nullable = false)
    private String formation;
    
    @Column(nullable = false)
    private String promotion;
    
    @Column(nullable = false)
    private Integer startYear;
    
    @Column
    private Integer endYear;
    
    @Column(nullable = false)
    private String email;
    
    @Column
    private String phoneNumber;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToMany
    @JoinTable(
        name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Training> courses = new ArrayList<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diploma> diplomas = new ArrayList<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OtherTraining> otherTrainings = new ArrayList<>();
    
    @Column
    private String employmentStatus;
    
    @Column
    private String company;
    
    @Column
    private String position;
    
    @Column
    private LocalDate startDate;
    
    @Column
    private String comments;
    
    @Column
    private String contactInfo;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Entity
    @Table(name = "diplomas")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EntityListeners(AuditingEntityListener.class)
    public static class Diploma {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id", nullable = false)
        private Student student;
        
        @Column(nullable = false)
        private String title;
        
        @Column(nullable = false)
        private LocalDate obtainedDate;
        
        @Column(nullable = false)
        private String institution;
        
        @Column
        private String description;
        
        @CreatedDate
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;
        
        @LastModifiedDate
        @Column(nullable = false)
        private LocalDateTime updatedAt;
    }
    
    @Entity
    @Table(name = "other_trainings")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EntityListeners(AuditingEntityListener.class)
    public static class OtherTraining {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id", nullable = false)
        private Student student;
        
        @Column(nullable = false)
        private String title;
        
        @Column(nullable = false)
        private LocalDate startDate;
        
        @Column(nullable = false)
        private LocalDate endDate;
        
        @Column(nullable = false)
        private String provider;
        
        @Column
        private String description;
        
        @CreatedDate
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;
        
        @LastModifiedDate
        @Column(nullable = false)
        private LocalDateTime updatedAt;
    }
}
