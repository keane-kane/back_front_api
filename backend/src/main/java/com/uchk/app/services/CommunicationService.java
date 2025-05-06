package com.uchk.app.services;

import com.uchk.app.dto.ReportDto;
import com.uchk.app.models.Report;
import com.uchk.app.models.User;
import com.uchk.app.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunicationService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<ReportDto> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReportDto getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));
        return convertToDto(report);
    }

    @Transactional(readOnly = true)
    public List<ReportDto> getRecentReports() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        return reportRepository.findRecentReports(threeMonthsAgo).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReportDto createReport(ReportDto reportDto) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        Report report = convertToEntity(reportDto);
        report.setCreatedBy(currentUser);
        
        Report savedReport = reportRepository.save(report);
        return convertToDto(savedReport);
    }

    @Transactional
    public ReportDto updateReport(Long id, ReportDto reportDto) {
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));
        
        // Check if current user is allowed to update this report
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        if (!currentUser.getId().equals(existingReport.getCreatedBy().getId()) && 
            !currentUser.hasRole("ROLE_ADMIN")) {
            throw new IllegalStateException("Not authorized to update this report");
        }
        
        // Update report fields but keep the creator
        existingReport.setTitle(reportDto.getTitle());
        existingReport.setType(Report.ReportType.valueOf(reportDto.getType()));
        existingReport.setDate(reportDto.getDate());
        existingReport.setContent(reportDto.getContent());
        existingReport.setParticipants(reportDto.getParticipants());
        
        Report updatedReport = reportRepository.save(existingReport);
        return convertToDto(updatedReport);
    }

    @Transactional
    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));
        
        // Check if current user is allowed to delete this report
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        if (!currentUser.getId().equals(report.getCreatedBy().getId()) && 
            !currentUser.hasRole("ROLE_ADMIN")) {
            throw new IllegalStateException("Not authorized to delete this report");
        }
        
        reportRepository.delete(report);
    }
    
    // Helper method to convert Report entity to ReportDto
    private ReportDto convertToDto(Report report) {
        return ReportDto.builder()
                .id(report.getId())
                .title(report.getTitle())
                .type(report.getType().name())
                .date(report.getDate())
                .content(report.getContent())
                .participants(report.getParticipants())
                .createdById(report.getCreatedBy().getId())
                .createdByName(report.getCreatedBy().getFullName())
                .createdAt(report.getCreatedAt())
                .build();
    }
    
    // Helper method to convert ReportDto to Report entity
    private Report convertToEntity(ReportDto reportDto) {
        return Report.builder()
                .title(reportDto.getTitle())
                .type(Report.ReportType.valueOf(reportDto.getType()))
                .date(reportDto.getDate())
                .content(reportDto.getContent())
                .participants(reportDto.getParticipants())
                .build();
    }
}
