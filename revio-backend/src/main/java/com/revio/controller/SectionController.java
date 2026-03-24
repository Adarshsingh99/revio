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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sections")
@Slf4j
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<List<SectionResponse>> getAllSections(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching sections for user: {}", userId);

        return new ResponseEntity<>(sectionService.getSectionsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> getSectionById(
            Authentication authentication,
            @PathVariable String sectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching section {} for user: {}", sectionId, userId);

        return new ResponseEntity<>(sectionService.getSectionById(userId, sectionId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(
            Authentication authentication,
            @Valid @RequestBody SectionRequest sectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Creating section for user: {}", userId);

        return new ResponseEntity<>(sectionService.createSection(userId, sectionRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(
            Authentication authentication,
            @PathVariable String sectionId,
            @Valid @RequestBody SectionRequest sectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Updating section {} for user: {}", sectionId, userId);

        return new ResponseEntity<>(sectionService.updateSection(userId, sectionId, sectionRequest), HttpStatus.OK);
    }

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
