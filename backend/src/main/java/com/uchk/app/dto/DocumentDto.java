package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    private String reference;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Content is required")
    @Size(max = 10000, message = "Content cannot exceed 10000 characters")
    private String content;
    
    @NotNull(message = "Visibility roles must be specified")
    private List<String> visibleTo;
    
    private Long createdById;
    private String creatorName;
    private LocalDateTime creationDate;
}
