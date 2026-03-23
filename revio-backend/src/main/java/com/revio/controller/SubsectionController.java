package com.revio.controller;

import com.revio.dto.SubsectionRequest;
import com.revio.dto.SubsectionResponse;
import com.revio.service.SubsectionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Subsection Controller for managing subsections within sections.
 * 
 * Endpoints:
 * - GET /sections/{sectionId}/subsections - Get all subsections in a section
 * - GET /sections/{sectionId}/subsections/{subsectionId} - Get specific subsection
 * - POST /sections/{sectionId}/subsections - Create new subsection
 * - PUT /sections/{sectionId}/subsections/{subsectionId} - Update subsection
 * - DELETE /sections/{sectionId}/subsections/{subsectionId} - Delete subsection
 * 
 * Requires authentication (JWT token).
 */
@RestController
@RequestMapping("/sections/{sectionId}/subsections")
@Slf4j
public class SubsectionController {

    @Autowired
    private SubsectionService subsectionService;

    /**
     * Get all subsections in a section.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @return ResponseEntity with list of SubsectionResponse objects
     */
    @GetMapping
    public ResponseEntity<List<SubsectionResponse>> getSubsections(
            Authentication authentication,
            @PathVariable String sectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching subsections for section {} by user: {}", sectionId, userId);
        
        List<SubsectionResponse> subsections = subsectionService.getSubsectionsBySection(userId, sectionId);
        return new ResponseEntity<>(subsections, HttpStatus.OK);
    }

    /**
     * Get a specific subsection.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @param subsectionId the subsection's ID
     * @return ResponseEntity with SubsectionResponse
     */
    @GetMapping("/{subsectionId}")
    public ResponseEntity<SubsectionResponse> getSubsectionById(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching subsection {} from section {}", subsectionId, sectionId);
        
        SubsectionResponse subsection = subsectionService.getSubsectionById(userId, sectionId, subsectionId);
        return new ResponseEntity<>(subsection, HttpStatus.OK);
    }

    /**
     * Create a new subsection.
     * 
     * Request body:
     * {
     *   "title": "Arrays",
     *   "description": "Array data structure and algorithms"
     * }
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @param subsectionRequest containing subsection details
     * @return ResponseEntity with created SubsectionResponse
     */
    @PostMapping
    public ResponseEntity<SubsectionResponse> createSubsection(
            Authentication authentication,
            @PathVariable String sectionId,
            @Valid @RequestBody SubsectionRequest subsectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Creating subsection for section {} by user: {}", sectionId, userId);
        
        SubsectionResponse response = subsectionService.createSubsection(userId, sectionId, subsectionRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update a subsection.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @param subsectionId the subsection's ID
     * @param subsectionRequest containing updated details
     * @return ResponseEntity with updated SubsectionResponse
     */
    @PutMapping("/{subsectionId}")
    public ResponseEntity<SubsectionResponse> updateSubsection(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId,
            @Valid @RequestBody SubsectionRequest subsectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Updating subsection {} in section {}", subsectionId, sectionId);
        
        SubsectionResponse response = subsectionService.updateSubsection(userId, sectionId, subsectionId, subsectionRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete a subsection.
     * 
     * @param authentication Spring Security authentication object
     * @param sectionId the section's ID
     * @param subsectionId the subsection's ID
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{subsectionId}")
    public ResponseEntity<String> deleteSubsection(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Deleting subsection {} from section {}", subsectionId, sectionId);
        
        subsectionService.deleteSubsection(userId, sectionId, subsectionId);
        return new ResponseEntity<>("Subsection deleted successfully", HttpStatus.OK);
    }
}
