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
@RequestMapping("/sections/{sectionId}/subsections")
@Slf4j
public class SubsectionController {

    @Autowired
    private SubsectionService subsectionService;

    @GetMapping
    public ResponseEntity<List<SubsectionResponse>> getSubsections(
            Authentication authentication,
            @PathVariable String sectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching subsections for section {} by user: {}", sectionId, userId);

        return new ResponseEntity<>(subsectionService.getSubsectionsBySection(userId, sectionId), HttpStatus.OK);
    }

    @GetMapping("/{subsectionId}")
    public ResponseEntity<SubsectionResponse> getSubsectionById(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching subsection {} from section {}", subsectionId, sectionId);

        return new ResponseEntity<>(subsectionService.getSubsectionById(userId, sectionId, subsectionId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SubsectionResponse> createSubsection(
            Authentication authentication,
            @PathVariable String sectionId,
            @Valid @RequestBody SubsectionRequest subsectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Creating subsection for section {} by user: {}", sectionId, userId);

        return new ResponseEntity<>(
                subsectionService.createSubsection(userId, sectionId, subsectionRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{subsectionId}")
    public ResponseEntity<SubsectionResponse> updateSubsection(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId,
            @Valid @RequestBody SubsectionRequest subsectionRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Updating subsection {} in section {}", subsectionId, sectionId);

        return new ResponseEntity<>(
                subsectionService.updateSubsection(userId, sectionId, subsectionId, subsectionRequest),
                HttpStatus.OK);
    }

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
