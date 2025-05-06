package com.uchk.app.controllers;

import com.uchk.app.dto.ReportDto;
import com.uchk.app.models.Report;
import com.uchk.app.services.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class CommunicationController {

    @Autowired
    private CommunicationService communicationService;

    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports() {
        List<ReportDto> reports = communicationService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long id) {
        ReportDto report = communicationService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ReportDto>> getRecentReports() {
        List<ReportDto> reports = communicationService.getRecentReports();
        return ResponseEntity.ok(reports);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<ReportDto> createReport(@Valid @RequestBody ReportDto reportDto) {
        ReportDto createdReport = communicationService.createReport(reportDto);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<ReportDto> updateReport(@PathVariable Long id, @Valid @RequestBody ReportDto reportDto) {
        ReportDto updatedReport = communicationService.updateReport(id, reportDto);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        communicationService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
