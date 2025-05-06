package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormationDto {
    
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Level is required")
    private String level;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    private Double cost;
    private String fundingType;
}
