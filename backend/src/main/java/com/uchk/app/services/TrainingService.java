package com.uchk.app.services;

import com.uchk.app.dto.CourseDto;
import com.uchk.app.dto.FormationDto;
import com.uchk.app.dto.InstructorDto;
import com.uchk.app.models.Instructor;
import com.uchk.app.models.Student;
import com.uchk.app.models.Training;
import com.uchk.app.models.User;
import com.uchk.app.repositories.InstructorRepository;
import com.uchk.app.repositories.StudentRepository;
import com.uchk.app.repositories.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return trainingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        Training course = trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        return convertToDto(course);
    }

    @Transactional
    public CourseDto createCourse(CourseDto courseDto) {
        Training.Formation formation = findFormation(courseDto.getFormationId());
        Instructor instructor = findInstructor(courseDto.getInstructorId());
        
        Training course = Training.builder()
                .title(courseDto.getTitle())
                .formation(formation)
                .instructor(instructor)
                .startDate(courseDto.getStartDate())
                .endDate(courseDto.getEndDate())
                .location(courseDto.getLocation())
                .description(courseDto.getDescription())
                .maxStudents(courseDto.getMaxStudents())
                .build();
        
        Training savedCourse = trainingRepository.save(course);
        return convertToDto(savedCourse);
    }

    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Training course = trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        
        Training.Formation formation = findFormation(courseDto.getFormationId());
        Instructor instructor = findInstructor(courseDto.getInstructorId());
        
        course.setTitle(courseDto.getTitle());
        course.setFormation(formation);
        course.setInstructor(instructor);
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setLocation(courseDto.getLocation());
        course.setDescription(courseDto.getDescription());
        course.setMaxStudents(courseDto.getMaxStudents());
        
        Training updatedCourse = trainingRepository.save(course);
        return convertToDto(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Training course = trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        
        // Remove the course from all students before deleting
        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
            studentRepository.save(student);
        }
        
        trainingRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FormationDto> getAllFormations() {
        List<Training.Formation> formations = trainingRepository.findAll().stream()
                .map(Training::getFormation)
                .distinct()
                .collect(Collectors.toList());
        
        return formations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getInstructorUpcomingCourses() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        Instructor instructor = instructorRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found for current user"));
        
        return trainingRepository.findUpcomingCoursesByInstructor(instructor.getId(), LocalDate.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getStudentSchedule() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        Student student = studentRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found for current user"));
        
        return trainingRepository.findByStudentId(student.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream generateSchedulePdf() {
        List<Training> courses = trainingRepository.findAll();
        
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Emploi du Temps - Université Cheikh Hamidou Kane", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add date
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC);
            Paragraph date = new Paragraph("Généré le: " + LocalDate.now().toString(), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);
            
            // Create table
            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Set table header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            PdfPCell headerCell;
            
            headerCell = new PdfPCell(new Phrase("Titre", headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Formation", headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Formateur", headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Période", headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Lieu", headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            // Add data rows
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            
            for (Training course : courses) {
                table.addCell(new Phrase(course.getTitle(), dataFont));
                table.addCell(new Phrase(course.getFormation().getName(), dataFont));
                table.addCell(new Phrase(course.getInstructor().getFullName(), dataFont));
                table.addCell(new Phrase(course.getStartDate() + " - " + course.getEndDate(), dataFont));
                table.addCell(new Phrase(course.getLocation(), dataFont));
            }
            
            document.add(table);
            document.close();
            
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    // Helper method to find Formation by ID
    private Training.Formation findFormation(Long formationId) {
        for (Training training : trainingRepository.findAll()) {
            if (training.getFormation().getId().equals(formationId)) {
                return training.getFormation();
            }
        }
        throw new EntityNotFoundException("Formation not found with id: " + formationId);
    }
    
    // Helper method to find Instructor by ID
    private Instructor findInstructor(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found with id: " + instructorId));
    }
    
    // Helper method to convert Training entity to CourseDto
    private CourseDto convertToDto(Training course) {
        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .formationId(course.getFormation().getId())
                .formationName(course.getFormation().getName())
                .instructorId(course.getInstructor().getId())
                .instructorName(course.getInstructor().getFullName())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .location(course.getLocation())
                .description(course.getDescription())
                .maxStudents(course.getMaxStudents())
                .build();
    }
    
    // Helper method to convert Instructor entity to InstructorDto
    private InstructorDto convertToDto(Instructor instructor) {
        return InstructorDto.builder()
                .id(instructor.getId())
                .firstName(instructor.getFirstName())
                .lastName(instructor.getLastName())
                .email(instructor.getEmail())
                .phoneNumber(instructor.getPhoneNumber())
                .type(instructor.getType().name())
                .specialization(instructor.getSpecialization())
                .build();
    }
    
    // Helper method to convert Formation entity to FormationDto
    private FormationDto convertToDto(Training.Formation formation) {
        return FormationDto.builder()
                .id(formation.getId())
                .name(formation.getName())
                .level(formation.getLevel())
                .type(formation.getType().name())
                .cost(formation.getCost())
                .fundingType(formation.getFundingType())
                .build();
    }
}
