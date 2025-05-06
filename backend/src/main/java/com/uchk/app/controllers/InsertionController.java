package com.uchk.app.controllers;

import com.uchk.app.dto.InsertionStatisticsDto;
import com.uchk.app.dto.StudentTrackingDto;
import com.uchk.app.dto.StudentDto;
import com.uchk.app.services.InsertionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/insertion")
public class InsertionController {

    @Autowired
    private InsertionService insertionService;

    @GetMapping("/graduates")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<List<StudentDto>> getAllGraduates() {
        List<StudentDto> graduates = insertionService.getAllGraduates();
        return ResponseEntity.ok(graduates);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<InsertionStatisticsDto> getInsertionStatistics() {
        InsertionStatisticsDto statistics = insertionService.getInsertionStatistics();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/tracking")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<StudentTrackingDto> updateStudentTracking(@Valid @RequestBody StudentTrackingDto trackingDto) {
        StudentTrackingDto updatedTracking = insertionService.updateStudentTracking(trackingDto);
        return ResponseEntity.ok(updatedTracking);
    }

    @DeleteMapping("/tracking/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<Void> resetStudentTracking(@PathVariable Long studentId) {
        insertionService.resetStudentTracking(studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-year")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<Map<Integer, List<StudentDto>>> getGraduatesByYear() {
        Map<Integer, List<StudentDto>> graduatesByYear = insertionService.getGraduatesByYear();
        return ResponseEntity.ok(graduatesByYear);
    }

    @GetMapping("/partners")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<List<Map<String, Object>>> getPartners() {
        List<Map<String, Object>> partners = insertionService.getPartners();
        return ResponseEntity.ok(partners);
    }
}
