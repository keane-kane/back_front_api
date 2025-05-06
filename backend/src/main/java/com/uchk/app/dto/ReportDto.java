package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotBlank(message = "Content is required")
    @Size(max = 5000, message = "Content cannot exceed 5000 characters")
    private String content;
    
    @Size(max = 500, message = "Participants cannot exceed 500 characters")
    private String participants;
    
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
