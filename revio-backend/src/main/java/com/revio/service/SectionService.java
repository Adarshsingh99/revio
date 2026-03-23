package com.revio.service;

import com.revio.dto.SectionRequest;
import com.revio.dto.SectionResponse;
import com.revio.exception.AccessDeniedException;
import com.revio.exception.ResourceNotFoundException;
import com.revio.model.Section;
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
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubsectionRepository subsectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    public SectionResponse createSection(String userId, SectionRequest sectionRequest) {
        Section section = Section.builder()
                .userId(userId)
                .title(sectionRequest.getTitle())
                .description(sectionRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return buildSectionResponse(sectionRepository.save(section));
    }

    public List<SectionResponse> getSectionsByUserId(String userId) {
        return sectionRepository.findByUserId(userId)
                .stream()
                .map(this::buildSectionResponse)
                .collect(Collectors.toList());
    }

    public SectionResponse getSectionById(String userId, String sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this section");
        }

        return buildSectionResponse(section);
    }

    public SectionResponse updateSection(String userId, String sectionId, SectionRequest sectionRequest) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to update this section");
        }

        section.setTitle(sectionRequest.getTitle());
        section.setDescription(sectionRequest.getDescription());
        section.setUpdatedAt(LocalDateTime.now());

        return buildSectionResponse(sectionRepository.save(section));
    }

    public void deleteSection(String userId, String sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to delete this section");
        }

        sectionRepository.deleteById(sectionId);
    }

    private SectionResponse buildSectionResponse(Section section) {
        List<String> subsectionIds = subsectionRepository.findBySectionId(section.getId())
                .stream()
                .map(s -> s.getId())
                .collect(Collectors.toList());

        long totalTopics = 0;
        long completedTopics = 0;

        for (String subsectionId : subsectionIds) {
            totalTopics += topicRepository.findBySubsectionId(subsectionId).size();
            completedTopics += topicRepository.countBySubsectionIdAndStatus(subsectionId, "COMPLETED");
        }

        return SectionResponse.builder()
                .id(section.getId())
                .userId(section.getUserId())
                .title(section.getTitle())
                .description(section.getDescription())
                .subsectionCount((long) subsectionIds.size())
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .build();
    }
}
