package com.revio.service;

import com.revio.dto.SubsectionRequest;
import com.revio.dto.SubsectionResponse;
import com.revio.exception.AccessDeniedException;
import com.revio.exception.ResourceNotFoundException;
import com.revio.model.Section;
import com.revio.model.Subsection;
import com.revio.repository.SectionRepository;
import com.revio.repository.SubsectionRepository;
import com.revio.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubsectionService {

    @Autowired
    private SubsectionRepository subsectionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    public SubsectionResponse createSubsection(String userId, String sectionId, SubsectionRequest subsectionRequest) {
        log.info("Creating subsection for section: {} by user: {}", sectionId, userId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to add subsections to this section");
        }

        Subsection subsection = Subsection.builder()
                .sectionId(sectionId)
                .title(subsectionRequest.getTitle())
                .description(subsectionRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return buildSubsectionResponse(subsectionRepository.save(subsection));
    }

    public List<SubsectionResponse> getSubsectionsBySection(String userId, String sectionId) {
        log.debug("Fetching subsections for section: {} by user: {}", sectionId, userId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this section");
        }

        return subsectionRepository.findBySectionId(sectionId)
                .stream()
                .map(this::buildSubsectionResponse)
                .collect(Collectors.toList());
    }

    public SubsectionResponse getSubsectionById(String userId, String sectionId, String subsectionId) {
        log.debug("Fetching subsection {} from section {}", subsectionId, sectionId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this section");
        }

        Subsection subsection = subsectionRepository.findByIdAndSectionId(subsectionId, sectionId);
        if (subsection == null) {
            throw new ResourceNotFoundException("Subsection not found with ID: " + subsectionId);
        }

        return buildSubsectionResponse(subsection);
    }

    public SubsectionResponse updateSubsection(
            String userId,
            String sectionId,
            String subsectionId,
            SubsectionRequest subsectionRequest) {
        log.info("Updating subsection {}", subsectionId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to update this subsection");
        }

        Subsection subsection = subsectionRepository.findByIdAndSectionId(subsectionId, sectionId);
        if (subsection == null) {
            throw new ResourceNotFoundException("Subsection not found");
        }

        subsection.setTitle(subsectionRequest.getTitle());
        subsection.setDescription(subsectionRequest.getDescription());
        subsection.setUpdatedAt(LocalDateTime.now());

        return buildSubsectionResponse(subsectionRepository.save(subsection));
    }

    public void deleteSubsection(String userId, String sectionId, String subsectionId) {
        log.info("Deleting subsection {}", subsectionId);

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to delete this subsection");
        }

        Subsection subsection = subsectionRepository.findByIdAndSectionId(subsectionId, sectionId);
        if (subsection == null) {
            throw new ResourceNotFoundException("Subsection not found");
        }

        subsectionRepository.deleteById(subsectionId);
    }

    private SubsectionResponse buildSubsectionResponse(Subsection subsection) {
        long topicCount = topicRepository.findBySubsectionId(subsection.getId()).size();
        long completedTopics = topicRepository.countBySubsectionIdAndStatus(subsection.getId(), "COMPLETED");
        long pendingTopics = topicCount - completedTopics;
        double progressPercentage = topicCount > 0 ? (completedTopics * 100.0) / topicCount : 0.0;

        return SubsectionResponse.builder()
                .id(subsection.getId())
                .sectionId(subsection.getSectionId())
                .title(subsection.getTitle())
                .description(subsection.getDescription())
                .topicCount(topicCount)
                .completedTopics(completedTopics)
                .pendingTopics(pendingTopics)
                .progressPercentage(progressPercentage)
                .createdAt(subsection.getCreatedAt())
                .updatedAt(subsection.getUpdatedAt())
                .build();
    }
}
