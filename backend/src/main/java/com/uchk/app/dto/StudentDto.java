package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    
    private Long id;
    private String ine;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String formation;
    private String promotion;
    private Integer startYear;
    private Integer endYear;
    private String email;
    private String phoneNumber;
    
    // Employment tracking data
    private String employmentStatus;
    private String company;
    private String position;
    private Integer graduationYear;
    
    // Related data
    private List<Object> courses = new ArrayList<>();
    private List<Object> diplomas = new ArrayList<>();
    private List<Object> otherTrainings = new ArrayList<>();
}
