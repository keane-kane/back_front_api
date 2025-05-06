package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTrackingDto {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Employment status is required")
    private String employmentStatus;
    
    private String company;
    private String position;
    private LocalDate startDate;
    private String comments;
    private String contactInfo;
}
