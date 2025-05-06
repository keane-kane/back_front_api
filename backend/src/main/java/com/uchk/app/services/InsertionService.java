package com.uchk.app.services;

import com.uchk.app.dto.InsertionStatisticsDto;
import com.uchk.app.dto.StudentDto;
import com.uchk.app.dto.StudentTrackingDto;
import com.uchk.app.models.Partner;
import com.uchk.app.models.Student;
import com.uchk.app.repositories.PartnerRepository;
import com.uchk.app.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InsertionService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Transactional(readOnly = true)
    public List<StudentDto> getAllGraduates() {
        return studentRepository.findAllGraduates().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InsertionStatisticsDto getInsertionStatistics() {
        InsertionStatisticsDto stats = new InsertionStatisticsDto();
        
        stats.setEmployedCount(studentRepository.countByEmploymentStatus("EMPLOYED"));
        stats.setSelfEmployedCount(studentRepository.countByEmploymentStatus("SELF_EMPLOYED"));
        stats.setSeekingCount(studentRepository.countByEmploymentStatus("SEEKING"));
        stats.setFurtherStudiesCount(studentRepository.countByEmploymentStatus("FURTHER_STUDIES"));
        stats.setInternshipCount(studentRepository.countByEmploymentStatus("INTERNSHIP"));
        stats.setUnknownCount(studentRepository.countByEmploymentStatus("UNKNOWN"));
        
        return stats;
    }

    @Transactional
    public StudentTrackingDto updateStudentTracking(StudentTrackingDto trackingDto) {
        Student student = studentRepository.findById(trackingDto.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + trackingDto.getStudentId()));
        
        student.setEmploymentStatus(trackingDto.getEmploymentStatus());
        student.setCompany(trackingDto.getCompany());
        student.setPosition(trackingDto.getPosition());
        student.setStartDate(trackingDto.getStartDate());
        student.setComments(trackingDto.getComments());
        student.setContactInfo(trackingDto.getContactInfo());
        
        Student updatedStudent = studentRepository.save(student);
        
        return StudentTrackingDto.builder()
                .studentId(updatedStudent.getId())
                .employmentStatus(updatedStudent.getEmploymentStatus())
                .company(updatedStudent.getCompany())
                .position(updatedStudent.getPosition())
                .startDate(updatedStudent.getStartDate())
                .comments(updatedStudent.getComments())
                .contactInfo(updatedStudent.getContactInfo())
                .build();
    }

    @Transactional
    public void resetStudentTracking(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        
        student.setEmploymentStatus("UNKNOWN");
        student.setCompany(null);
        student.setPosition(null);
        student.setStartDate(null);
        student.setComments(null);
        student.setContactInfo(null);
        
        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<StudentDto>> getGraduatesByYear() {
        List<Student> allGraduates = studentRepository.findAllGraduates();
        
        return allGraduates.stream()
                .collect(Collectors.groupingBy(
                        Student::getEndYear,
                        Collectors.mapping(this::convertToDto, Collectors.toList())
                ));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPartners() {
        return partnerRepository.findAll().stream()
                .map(this::convertPartnerToMap)
                .collect(Collectors.toList());
    }
    
    // Helper method to convert Student entity to StudentDto for the insertion module
    private StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .ine(student.getIne())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .formation(student.getFormation())
                .promotion(student.getPromotion())
                .startYear(student.getStartYear())
                .endYear(student.getEndYear())
                .employmentStatus(student.getEmploymentStatus())
                .company(student.getCompany())
                .position(student.getPosition())
                .graduationYear(student.getEndYear())
                .build();
    }
    
    // Helper method to convert Partner entity to a Map for the frontend
    private Map<String, Object> convertPartnerToMap(Partner partner) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("id", partner.getId());
        result.put("name", partner.getName());
        result.put("type", partner.getType().name());
        result.put("contactPerson", partner.getContactPerson());
        result.put("email", partner.getEmail());
        result.put("phoneNumber", partner.getPhoneNumber());
        result.put("description", partner.getDescription());
        result.put("partnershipStartDate", partner.getPartnershipStartDate());
        result.put("partnershipEndDate", partner.getPartnershipEndDate());
        result.put("active", partner.getPartnershipEndDate() == null || partner.getPartnershipEndDate().isAfter(LocalDate.now()));
        
        return result;
    }
}
