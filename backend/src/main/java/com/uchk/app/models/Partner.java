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
@Table(name = "partners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PartnerType type;
    
    @Column
    private String contactPerson;
    
    @Column
    private String email;
    
    @Column
    private String phoneNumber;
    
    @Column
    private String address;
    
    @Column(length = 500)
    private String description;
    
    @Column
    private LocalDate partnershipStartDate;
    
    @Column
    private LocalDate partnershipEndDate;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum PartnerType {
        ACADEMIC,    // Académique
        CORPORATE,   // Entreprise
        GOVERNMENT,  // Gouvernement
        NGO,         // ONG
        OTHER        // Autre
    }
}
