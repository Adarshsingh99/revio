package com.revio.controller;

import com.revio.dto.SectionRequest;
import com.revio.dto.SectionResponse;
import com.revio.service.SectionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Section Controller for managing study sections.
 * 
 * Endpoints:
 * - GET /sections - Get all sections for authenticated user
 * - GET /sections/{id} - Get specific section
 * - POST /sections - Create new section
 * - PUT /sections/{id} - Update section
 * - DELETE /sections/{id} - Delete section
 * 
 * Requires authentication (JWT token).
 */
@RestController
@RequestMapping("/sections")
@Slf4j
public class SectionController {

    @Autowired
    private SectionService sectionService;

    /**
     * Get all sections for the authenticated user.
     * 
     * @param authentication Spring Security authentication object (contains userId)
     * @return ResponseEntity with list of SectionResponse objects
     */
    @GetMapping
    public ResponseEntity<List<SectionResponse>> getAllSections(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching sections for user: {}", userId);
        
        List<SectionResponse> sections = sectionService.getSectionsByUserId(userId);
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    /**
     * Get a specific section by ID.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @return ResponseEntity with SectionResponse
     */
    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> getSectionById(
            Authentication authentication,
            @PathVariable String sectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching section {} for user: {}", sectionId, userId);
        
        SectionResponse section = sectionService.getSectionById(userId, sectionId);
        return new ResponseEntity<>(section, HttpStatus.OK);
    }

    /**
     * Create a new section.
     * 
     * Request body:
     * {
     *   "title": "Data Structures and Algorithms",
     *   "description": "Learn DSA fundamentals"
     * }
     * 
     * @param authentication Spring Security authentication object
     * @param sectionRequest containing section details
     * @return ResponseEntity with created SectionResponse
     */
    @PostMapping
    public ResponseEntity<SectionResponse> createSection(
            Authentication authentication,
            @Valid @RequestBody SectionRequest sectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Creating section for user: {}", userId);
        
        SectionResponse response = sectionService.createSection(userId, sectionRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update a section.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @param sectionRequest containing updated details
     * @return ResponseEntity with updated SectionResponse
     */
    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(
            Authentication authentication,
            @PathVariable String sectionId,
            @Valid @RequestBody SectionRequest sectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Updating section {} for user: {}", sectionId, userId);
        
        SectionResponse response = sectionService.updateSection(userId, sectionId, sectionRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete a section.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<String> deleteSection(
            Authentication authentication,
            @PathVariable String sectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Deleting section {} for user: {}", sectionId, userId);
        
        sectionService.deleteSection(userId, sectionId);
        return new ResponseEntity<>("Section deleted successfully", HttpStatus.OK);
    }
}
