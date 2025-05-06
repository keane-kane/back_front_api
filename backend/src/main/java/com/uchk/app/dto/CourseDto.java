package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    
    @NotNull(message = "Formation is required")
    private Long formationId;
    private String formationName;
    
    @NotNull(message = "Instructor is required")
    private Long instructorId;
    private String instructorName;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private String description;
    
    @NotNull(message = "Maximum number of students is required")
    @Min(value = 1, message = "Maximum number of students must be at least 1")
    private Integer maxStudents;
}
