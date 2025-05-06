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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private String phoneNumber;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstructorType type;
    
    @Column
    private String specialization;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "instructor")
    private List<Training> courses = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum InstructorType {
        TEACHER,        // Enseignant
        ASSOCIATE,      // Enseignant associé
        PROGRAM_HEAD,   // Responsable de formation
        TUTOR           // Tuteur
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
