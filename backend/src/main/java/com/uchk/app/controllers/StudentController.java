package com.uchk.app.controllers;

import com.uchk.app.dto.StudentDto;
import com.uchk.app.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<StudentDto> getCurrentStudentProfile() {
        StudentDto studentProfile = studentService.getCurrentStudentProfile();
        return ResponseEntity.ok(studentProfile);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE') or @studentService.isCurrentStudent(#id)")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        StudentDto updatedStudent = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<StudentDto>> searchStudents(@RequestParam String query) {
        List<StudentDto> students = studentService.searchStudents(query);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/by-formation/{formationId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<StudentDto>> getStudentsByFormation(@PathVariable Long formationId) {
        List<StudentDto> students = studentService.getStudentsByFormation(formationId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/by-promotion/{promotion}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<StudentDto>> getStudentsByPromotion(@PathVariable String promotion) {
        List<StudentDto> students = studentService.getStudentsByPromotion(promotion);
        return ResponseEntity.ok(students);
    }
}
