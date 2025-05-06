package com.uchk.app.services;

import com.uchk.app.dto.DocumentDto;
import com.uchk.app.models.Document;
import com.uchk.app.models.User;
import com.uchk.app.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministrationService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<DocumentDto> getAllDocuments() {
        User currentUser = userService.getCurrentUser();
        List<Document> documents = documentRepository.findAll();
        
        // Filter documents based on user's roles
        return documents.stream()
                .filter(doc -> isDocumentVisibleToUser(doc, currentUser))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DocumentDto getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (!isDocumentVisibleToUser(document, currentUser)) {
            throw new IllegalStateException("Not authorized to view this document");
        }
        
        return convertToDto(document);
    }

    @Transactional
    public DocumentDto createDocument(DocumentDto documentDto) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        Document document = convertToEntity(documentDto);
        document.setCreatedBy(currentUser);
        
        Document savedDocument = documentRepository.save(document);
        return convertToDto(savedDocument);
    }

    @Transactional
    public DocumentDto updateDocument(Long id, DocumentDto documentDto) {
        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        if (!currentUser.getId().equals(existingDocument.getCreatedBy().getId()) && 
            !currentUser.hasRole("ROLE_ADMIN")) {
            throw new IllegalStateException("Not authorized to update this document");
        }
        
        // Update document fields but keep the creator
        existingDocument.setTitle(documentDto.getTitle());
        existingDocument.setType(Document.DocumentType.valueOf(documentDto.getType()));
        existingDocument.setReference(documentDto.getReference());
        existingDocument.setDescription(documentDto.getDescription());
        existingDocument.setContent(documentDto.getContent());
        existingDocument.setVisibleTo(new HashSet<>(documentDto.getVisibleTo()));
        
        Document updatedDocument = documentRepository.save(existingDocument);
        return convertToDto(updatedDocument);
    }

    @Transactional
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        if (!currentUser.getId().equals(document.getCreatedBy().getId()) && 
            !currentUser.hasRole("ROLE_ADMIN")) {
            throw new IllegalStateException("Not authorized to delete this document");
        }
        
        documentRepository.delete(document);
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> searchDocuments(String query) {
        User currentUser = userService.getCurrentUser();
        
        return documentRepository.search(query).stream()
                .filter(doc -> isDocumentVisibleToUser(doc, currentUser))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByType(String type) {
        User currentUser = userService.getCurrentUser();
        Document.DocumentType documentType = Document.DocumentType.valueOf(type);
        
        return documentRepository.findByType(documentType).stream()
                .filter(doc -> isDocumentVisibleToUser(doc, currentUser))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Helper method to check if a document is visible to a user based on roles
    private boolean isDocumentVisibleToUser(Document document, User user) {
        if (user == null) return false;
        
        // Admins can see all documents
        if (user.hasRole("ROLE_ADMIN")) return true;
        
        // Creator can always see their own documents
        if (document.getCreatedBy().getId().equals(user.getId())) return true;
        
        // Check if any of the user's roles are in the document's visibleTo set
        return user.getRoles().stream()
                .anyMatch(role -> document.getVisibleTo().contains(role.getName()));
    }
    
    // Helper method to convert Document entity to DocumentDto
    private DocumentDto convertToDto(Document document) {
        return DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .type(document.getType().name())
                .reference(document.getReference())
                .description(document.getDescription())
                .content(document.getContent())
                .visibleTo(new ArrayList<>(document.getVisibleTo()))
                .createdById(document.getCreatedBy().getId())
                .creatorName(document.getCreatedBy().getFullName())
                .creationDate(document.getCreationDate())
                .build();
    }
    
    // Helper method to convert DocumentDto to Document entity
    private Document convertToEntity(DocumentDto documentDto) {
        return Document.builder()
                .title(documentDto.getTitle())
                .type(Document.DocumentType.valueOf(documentDto.getType()))
                .reference(documentDto.getReference())
                .description(documentDto.getDescription())
                .content(documentDto.getContent())
                .visibleTo(new HashSet<>(documentDto.getVisibleTo()))
                .build();
    }
    
    // ArrayList needs to be imported
    private static class ArrayList<T> extends java.util.ArrayList<T> {
        public ArrayList(java.util.Collection<? extends T> c) {
            super(c);
        }
    }
}
