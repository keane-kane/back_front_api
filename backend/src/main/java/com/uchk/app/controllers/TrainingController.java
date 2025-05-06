package com.uchk.app.controllers;

import com.uchk.app.dto.CourseDto;
import com.uchk.app.dto.InstructorDto;
import com.uchk.app.dto.FormationDto;
import com.uchk.app.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    // Course endpoints
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = trainingService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto course = trainingService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/courses")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) {
        CourseDto createdCourse = trainingService.createCourse(courseDto);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PutMapping("/courses/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        CourseDto updatedCourse = trainingService.updateCourse(id, courseDto);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/courses/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        trainingService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/export")
    public ResponseEntity<InputStreamResource> exportSchedule() {
        ByteArrayInputStream bis = trainingService.generateSchedulePdf();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=emploi_du_temps.pdf");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // Instructor endpoints
    @GetMapping("/instructors")
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        List<InstructorDto> instructors = trainingService.getAllInstructors();
        return ResponseEntity.ok(instructors);
    }

    // Formation endpoints
    @GetMapping("/formations")
    public ResponseEntity<List<FormationDto>> getAllFormations() {
        List<FormationDto> formations = trainingService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/instructor/upcoming")
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<List<CourseDto>> getInstructorUpcomingCourses() {
        List<CourseDto> courses = trainingService.getInstructorUpcomingCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/student/schedule")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<List<CourseDto>> getStudentSchedule() {
        List<CourseDto> courses = trainingService.getStudentSchedule();
        return ResponseEntity.ok(courses);
    }
}
