package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {
    
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Pattern(regexp = "^[0-9]{9,15}$", message = "Phone number should be valid")
    private String phoneNumber;
    
    @NotBlank(message = "Instructor type is required")
    private String type;
    
    private String specialization;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
