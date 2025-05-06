package com.uchk.app.controllers;

import com.uchk.app.dto.DocumentDto;
import com.uchk.app.services.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class AdministrationController {

    @Autowired
    private AdministrationService administrationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        List<DocumentDto> documents = administrationService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
        DocumentDto document = administrationService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<DocumentDto> createDocument(@Valid @RequestBody DocumentDto documentDto) {
        DocumentDto createdDocument = administrationService.createDocument(documentDto);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentDto documentDto) {
        DocumentDto updatedDocument = administrationService.updateDocument(id, documentDto);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        administrationService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<List<DocumentDto>> searchDocuments(@RequestParam String query) {
        List<DocumentDto> documents = administrationService.searchDocuments(query);
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/by-type/{type}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVE')")
    public ResponseEntity<List<DocumentDto>> getDocumentsByType(@PathVariable String type) {
        List<DocumentDto> documents = administrationService.getDocumentsByType(type);
        return ResponseEntity.ok(documents);
    }
}
