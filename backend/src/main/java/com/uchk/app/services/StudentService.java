package com.uchk.app.services;

import com.uchk.app.dto.StudentDto;
import com.uchk.app.models.Student;
import com.uchk.app.models.User;
import com.uchk.app.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        return convertToDto(student);
    }

    @Transactional(readOnly = true)
    public StudentDto getCurrentStudentProfile() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        Student student = studentRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Student profile not found for current user"));
        
        return convertToDto(student);
    }

    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        
        // Check if this is current student or admin
        if (!isCurrentStudent(id) && !isAdminOrAdministrative()) {
            throw new IllegalStateException("Not authorized to update this student profile");
        }
        
        student.setIne(studentDto.getIne());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setDateOfBirth(studentDto.getDateOfBirth());
        student.setFormation(studentDto.getFormation());
        student.setPromotion(studentDto.getPromotion());
        student.setStartYear(studentDto.getStartYear());
        student.setEndYear(studentDto.getEndYear());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        
        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    @Transactional(readOnly = true)
    public List<StudentDto> searchStudents(String query) {
        return studentRepository.search(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsByFormation(Long formationId) {
        // In a real implementation, we would have a direct query to find by formationId
        // For this project, we'll just filter by formation name
        return studentRepository.findAll().stream()
                .filter(student -> student.getFormation().contains(formationId.toString()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsByPromotion(String promotion) {
        return studentRepository.findByPromotion(promotion).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean isCurrentStudent(Long studentId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null || student.getUser() == null) {
            return false;
        }
        
        return student.getUser().getId().equals(currentUser.getId());
    }

    private boolean isAdminOrAdministrative() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.hasRole("ROLE_ADMIN") || currentUser.hasRole("ROLE_ADMINISTRATIVE");
    }
    
    // Helper method to convert Student entity to StudentDto
    private StudentDto convertToDto(Student student) {
        StudentDto dto = StudentDto.builder()
                .id(student.getId())
                .ine(student.getIne())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .formation(student.getFormation())
                .promotion(student.getPromotion())
                .startYear(student.getStartYear())
                .endYear(student.getEndYear())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .employmentStatus(student.getEmploymentStatus())
                .courses(new ArrayList<>())
                .diplomas(new ArrayList<>())
                .otherTrainings(new ArrayList<>())
                .build();
        
        // Add courses
        if (student.getCourses() != null) {
            student.getCourses().forEach(course -> {
                dto.getCourses().add(CourseBasicDto.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .startDate(course.getStartDate())
                        .endDate(course.getEndDate())
                        .location(course.getLocation())
                        .instructorName(course.getInstructor().getFullName())
                        .description(course.getDescription())
                        .build());
            });
        }
        
        // Add diplomas
        if (student.getDiplomas() != null) {
            student.getDiplomas().forEach(diploma -> {
                dto.getDiplomas().add(DiplomaDto.builder()
                        .id(diploma.getId())
                        .title(diploma.getTitle())
                        .obtainedDate(diploma.getObtainedDate())
                        .institution(diploma.getInstitution())
                        .description(diploma.getDescription())
                        .build());
            });
        }
        
        // Add other trainings
        if (student.getOtherTrainings() != null) {
            student.getOtherTrainings().forEach(training -> {
                dto.getOtherTrainings().add(OtherTrainingDto.builder()
                        .id(training.getId())
                        .title(training.getTitle())
                        .startDate(training.getStartDate())
                        .endDate(training.getEndDate())
                        .provider(training.getProvider())
                        .description(training.getDescription())
                        .build());
            });
        }
        
        return dto;
    }
    
    // Inner classes for DTO conversion
    
    @lombok.Data
    @lombok.Builder
    public static class CourseBasicDto {
        private Long id;
        private String title;
        private java.time.LocalDate startDate;
        private java.time.LocalDate endDate;
        private String location;
        private String instructorName;
        private String description;
    }
    
    @lombok.Data
    @lombok.Builder
    public static class DiplomaDto {
        private Long id;
        private String title;
        private java.time.LocalDate obtainedDate;
        private String institution;
        private String description;
    }
    
    @lombok.Data
    @lombok.Builder
    public static class OtherTrainingDto {
        private Long id;
        private String title;
        private java.time.LocalDate startDate;
        private java.time.LocalDate endDate;
        private String provider;
        private String description;
    }
}
