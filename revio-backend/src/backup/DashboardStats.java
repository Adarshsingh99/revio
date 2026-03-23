package com.revio.service;

import com.revio.dto.RevisionTopicResponse;
import com.revio.dto.TopicRequest;
import com.revio.dto.TopicResponse;
import com.revio.exception.AccessDeniedException;
import com.revio.exception.ResourceNotFoundException;
import com.revio.model.Section;
import com.revio.model.Subsection;
import com.revio.model.Topic;
import com.revio.repository.SectionRepository;
import com.revio.repository.SubsectionRepository;
import com.revio.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
@Service
@Slf4j
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubsectionRepository subsectionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    /**
     * Create a new topic under a subsection.
     * 
     * @param subsectionId the ID of the parent subsection
     * @param topicRequest the topic data
     * @return TopicResponse with created topic details
     * @throws ResourceNotFoundException if subsection not found
     */
    public TopicResponse createTopic(String subsectionId, TopicRequest topicRequest) {
        log.info("Creating topic '{}' under subsection {}", topicRequest.getTitle(), subsectionId);

        // Verify subsection exists
        Subsection subsection = subsectionRepository.findById(subsectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subsection not found with ID: " + subsectionId));

        // Create topic
        Topic topic = Topic.builder()
                .subsectionId(subsectionId)
                .title(topicRequest.getTitle())
                .description(topicRequest.getDescription())
                .status("PENDING")
                .revisionDates(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Topic savedTopic = topicRepository.save(topic);
        log.info("Created topic with ID: {}", savedTopic.getId());

        return TopicResponse.builder()
                .id(savedTopic.getId())
                .subsectionId(savedTopic.getSubsectionId())
                .title(savedTopic.getTitle())
                .description(savedTopic.getDescription())
                .status(savedTopic.getStatus())
                .completedAt(savedTopic.getCompletedAt())
                .revisionDates(savedTopic.getRevisionDates())
                .nextRevision(savedTopic.getNextRevision())
                .createdAt(savedTopic.getCreatedAt())
                .updatedAt(savedTopic.getUpdatedAt())
                .build();
    }

    /**
     * Get topic by ID.
     * 
     * @param topicId the topic ID
     * @return TopicResponse
     * @throws ResourceNotFoundException if topic not found
     */
    public TopicResponse getTopicById(String topicId) {
        log.debug("Fetching topic with ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));

        return TopicResponse.builder()
                .id(topic.getId())
                .subsectionId(topic.getSubsectionId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .status(topic.getStatus())
                .completedAt(topic.getCompletedAt())
                .revisionDates(topic.getRevisionDates())
                .nextRevision(topic.getNextRevision())
                .createdAt(topic.getCreatedAt())
                .updatedAt(topic.getUpdatedAt())
                .build();
    }

    /**
     * Update topic.
     * 
     * @param topicId the topic ID
     * @param topicRequest the updated data
     * @return TopicResponse
     * @throws ResourceNotFoundException if topic not found
     */
    public TopicResponse updateTopic(String topicId, TopicRequest topicRequest) {
        log.info("Updating topic {}", topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));

        topic.setTitle(topicRequest.getTitle());
        topic.setDescription(topicRequest.getDescription());
        topic.setUpdatedAt(LocalDateTime.now());

        Topic savedTopic = topicRepository.save(topic);
        log.info("Updated topic {}", topicId);

        return TopicResponse.builder()
                .id(savedTopic.getId())
                .subsectionId(savedTopic.getSubsectionId())
                .title(savedTopic.getTitle())
                .description(savedTopic.getDescription())
                .status(savedTopic.getStatus())
                .completedAt(savedTopic.getCompletedAt())
                .revisionDates(savedTopic.getRevisionDates())
                .nextRevision(savedTopic.getNextRevision())
                .createdAt(savedTopic.getCreatedAt())
                .updatedAt(savedTopic.getUpdatedAt())
                .build();
    }

    /**
     * Delete topic.
     * 
     * @param topicId the topic ID
     * @throws ResourceNotFoundException if topic not found
     */
    public void deleteTopic(String topicId) {
        log.info("Deleting topic {}", topicId);

        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic not found with ID: " + topicId);
        }

        topicRepository.deleteById(topicId);
        log.info("Deleted topic {}", topicId);
    }

    /**
     * Mark topic as complete and generate revision schedule.
     * 
     * @param topicId the topic ID
     * @return TopicResponse
     * @throws ResourceNotFoundException if topic not found
     */
    public TopicResponse markTopicComplete(String topicId) {
        log.info("Marking topic {} as complete", topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        topic.setStatus("COMPLETED");
        topic.setCompletedAt(now);

        // Generate revision dates
        List<LocalDate> revisionDates = new ArrayList<>();
        revisionDates.add(today); // Day 1: Today
        revisionDates.add(today.plusDays(1)); // Day 2: +1 day
        revisionDates.add(today.plusDays(4)); // Day 3: +4 days (3+1)
        revisionDates.add(today.plusDays(11)); // Day 4: +11 days (4+7)

        topic.setRevisionDates(revisionDates);
        topic.setNextRevision(today);
        topic.setUpdatedAt(now);

        Topic savedTopic = topicRepository.save(topic);
        log.info("Marked topic {} as complete with revision schedule", topicId);

        return TopicResponse.builder()
                .id(savedTopic.getId())
                .subsectionId(savedTopic.getSubsectionId())
                .title(savedTopic.getTitle())
                .description(savedTopic.getDescription())
                .status(savedTopic.getStatus())
                .completedAt(savedTopic.getCompletedAt())
                .revisionDates(savedTopic.getRevisionDates())
                .nextRevision(savedTopic.getNextRevision())
                .createdAt(savedTopic.getCreatedAt())
                .updatedAt(savedTopic.getUpdatedAt())
                .build();
    }

    /**
     * Get topics for today's revision.
     * 
     * @return list of TopicResponse
     */
    public List<RevisionTopicResponse> getTodayRevisionTopics() {
        log.debug("Fetching today's revision topics");

        LocalDate today = LocalDate.now();
        List<Topic> topics = topicRepository.findByNextRevision(today);

        return topics.stream()
                .map(topic -> RevisionTopicResponse.builder()
                        .id(topic.getId())
                        .title(topic.getTitle())
                        .description(topic.getDescription())
                        .subsectionId(topic.getSubsectionId())
                        .nextRevision(topic.getNextRevision())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Update next revision date for a topic.
     * 
     * @param topicId the topic ID
     * @return TopicResponse
     */
    public TopicResponse updateNextRevision(String topicId) {
        log.info("Updating next revision for topic {}", topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));

        LocalDate currentNext = topic.getNextRevision();
        List<LocalDate> revisionDates = topic.getRevisionDates();

        if (revisionDates != null && !revisionDates.isEmpty()) {
            int currentIndex = revisionDates.indexOf(currentNext);
            if (currentIndex >= 0 && currentIndex < revisionDates.size() - 1) {
                LocalDate nextDate = revisionDates.get(currentIndex + 1);
                topic.setNextRevision(nextDate);
                topic.setUpdatedAt(LocalDateTime.now());
                Topic savedTopic = topicRepository.save(topic);
                log.info("Updated next revision for topic {} to {}", topicId, nextDate);

                return TopicResponse.builder()
                        .id(savedTopic.getId())
                        .subsectionId(savedTopic.getSubsectionId())
                        .title(savedTopic.getTitle())
                        .description(savedTopic.getDescription())
                        .status(savedTopic.getStatus())
                        .completedAt(savedTopic.getCompletedAt())
                        .revisionDates(savedTopic.getRevisionDates())
                        .nextRevision(savedTopic.getNextRevision())
                        .createdAt(savedTopic.getCreatedAt())
                        .updatedAt(savedTopic.getUpdatedAt())
                        .build();
            }
        }

        // No more revisions
        topic.setNextRevision(null);
        topic.setUpdatedAt(LocalDateTime.now());
        Topic savedTopic = topicRepository.save(topic);
        log.info("No more revisions for topic {}", topicId);

        return TopicResponse.builder()
                .id(savedTopic.getId())
                .subsectionId(savedTopic.getSubsectionId())
                .title(savedTopic.getTitle())
                .description(savedTopic.getDescription())
                .status(savedTopic.getStatus())
                .completedAt(savedTopic.getCompletedAt())
                .revisionDates(savedTopic.getRevisionDates())
                .nextRevision(savedTopic.getNextRevision())
                .createdAt(savedTopic.getCreatedAt())
                .updatedAt(savedTopic.getUpdatedAt())
                .build();
    }

    /**
     * Get dashboard statistics.
     * 
     * @return DashboardStats
     */
    public DashboardStats getDashboardStats() {
        log.debug("Computing dashboard statistics");

        long totalTopics = topicRepository.count();
        long completedTopics = topicRepository.countByStatus("COMPLETED");
        long pendingTopics = totalTopics - completedTopics;
        double completionPercentage = totalTopics > 0 ? (double) completedTopics / totalTopics * 100 : 0;

        LocalDate today = LocalDate.now();
        long todayRevisionCount = topicRepository.countByNextRevision(today);

        // Get total sections (this is a simple count, in real app might need section repository)
        long totalSections = 0; // Placeholder

        return DashboardStats.builder()
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .pendingTopics(pendingTopics)
                .completionPercentage(completionPercentage)
                .todayRevisionCount(todayRevisionCount)
                .totalSections(totalSections)
                .build();
    }

    @lombok.Builder
    public static class DashboardStats {
        private Long totalTopics;
        private Long completedTopics;
        private Long pendingTopics;
        private Double completionPercentage;
        private Long todayRevisionCount;
        private Long totalSections;
    }
}
*/

    /**
     * Create a new topic.
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param subsectionId the subsection's ID
     * @param topicRequest containing topic details
     * @return TopicResponse
     */
    public TopicResponse createTopic(String userId, String sectionId, String subsectionId, 
                                     TopicRequest topicRequest) {
        log.info("Creating topic for subsection: {} by user: {}", subsectionId, userId);

        // Verify user owns the section
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to add topics to this section");
        }

        // Verify subsection exists and belongs to the section
        Subsection subsection = subsectionRepository.findByIdAndSectionId(subsectionId, sectionId);
        if (subsection == null) {
            throw new ResourceNotFoundException("Subsection not found");
        }

        Topic topic = Topic.builder()
                .subsectionId(subsectionId)
                .title(topicRequest.getTitle())
                .description(topicRequest.getDescription())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Topic savedTopic = topicRepository.save(topic);
        log.info("Topic created with ID: {}", savedTopic.getId());

        return buildTopicResponse(savedTopic);
    }

    /**
     * Get all topics in a subsection.
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param subsectionId the subsection's ID
     * @return List of TopicResponse objects
     */
    public List<TopicResponse> getTopicsBySubsection(String userId, String sectionId, String subsectionId) {
        log.debug("Fetching topics for subsection: {}", subsectionId);

        // Verify ownership
        verifyAccess(userId, sectionId);

        List<Topic> topics = topicRepository.findBySubsectionId(subsectionId);
        return topics.stream()
                .map(this::buildTopicResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific topic.
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param topicId the topic's ID
     * @return TopicResponse
     */
    public TopicResponse getTopicById(String userId, String sectionId, String topicId) {
        log.debug("Fetching topic: {}", topicId);

        verifyAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        return buildTopicResponse(topic);
    }

    /**
     * Update a topic.
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param topicId the topic's ID
     * @param topicRequest containing updated details
     * @return updated TopicResponse
     */
    public TopicResponse updateTopic(String userId, String sectionId, String topicId, TopicRequest topicRequest) {
        log.info("Updating topic: {}", topicId);

        verifyAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        topic.setTitle(topicRequest.getTitle());
        topic.setDescription(topicRequest.getDescription());
        topic.setUpdatedAt(LocalDateTime.now());

        Topic updatedTopic = topicRepository.save(topic);
        log.info("Topic {} updated successfully", topicId);

        return buildTopicResponse(updatedTopic);
    }

    /**
     * Delete a topic.
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param topicId the topic's ID
     */
    public void deleteTopic(String userId, String sectionId, String topicId) {
        log.info("Deleting topic: {}", topicId);

        verifyAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        topicRepository.deleteById(topicId);
        log.info("Topic {} deleted successfully", topicId);
    }

    /**
     * ⭐ CORE FEATURE: Mark topic as complete and generate revision dates.
     * 
     * This implements the smart revision scheduling system:
     * - Day 1: Today (completion day)
     * - Day 2: +1 day from today
     * - Day 3: +4 days from today (3 days after Day 2)
     * - Day 4: +11 days from today (7 days after Day 3)
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param topicId the topic's ID
     * @return TopicResponse with generated revision dates
     */
    public TopicResponse completeTopicAndScheduleRevision(String userId, String sectionId, String topicId) {
        log.info("Marking topic {} as complete and scheduling revisions", topicId);

        verifyAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        // Mark as completed
        topic.setStatus("COMPLETED");
        topic.setCompletedAt(LocalDateTime.now());

        // Generate revision dates using spaced repetition algorithm
        LocalDate today = LocalDate.now();
        List<LocalDate> revisionDates = new ArrayList<>();

        // Day 1: Today (completion day)
        revisionDates.add(today);

        // Day 2: +1 day
        revisionDates.add(today.plusDays(1));

        // Day 3: +4 days from today (3 days after Day 2)
        revisionDates.add(today.plusDays(4));

        // Day 4: +11 days from today (7 days after Day 3)
        revisionDates.add(today.plusDays(11));

        topic.setRevisionDates(revisionDates);
        
        // Set nextRevision to today (first revision is today)
        topic.setNextRevision(today);

        Topic completedTopic = topicRepository.save(topic);
        log.info("Topic {} marked as complete with revision schedule: {}", topicId, revisionDates);

        return buildTopicResponse(completedTopic);
    }

    /**
     * Mark a topic's revision as done and schedule the next revision.
     * 
     * @param userId the user's ID
     * @param sectionId the section's ID
     * @param topicId the topic's ID
     * @return TopicResponse with updated nextRevision date
     */
    public TopicResponse completeRevisionForTopic(String userId, String sectionId, String topicId) {
        log.info("Completing revision for topic: {}", topicId);

        verifyAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        if (topic.getRevisionDates() == null || topic.getRevisionDates().isEmpty()) {
            throw new ResourceNotFoundException("Topic has no scheduled revisions");
        }

        // Find the next revision date from the list
        LocalDate currentNextRevision = topic.getNextRevision();
        List<LocalDate> revisionDates = topic.getRevisionDates();

        // Find index of current revision
        int currentIndex = revisionDates.indexOf(currentNextRevision);

        if (currentIndex >= 0 && currentIndex < revisionDates.size() - 1) {
            // Set next revision to the following date
            LocalDate nextRevisionDate = revisionDates.get(currentIndex + 1);
            topic.setNextRevision(nextRevisionDate);
            log.info("Next revision scheduled for topic {} on {}", topicId, nextRevisionDate);
        } else {
            // All revisions completed
            topic.setNextRevision(null);
            topic.setStatus("FULLY_REVISED");
            log.info("All revisions completed for topic {}", topicId);
        }

        Topic updatedTopic = topicRepository.save(topic);
        return buildTopicResponse(updatedTopic);
    }

    /**
     * Get all topics that need revision today.
     * 
     * @param userId the user's ID
     * @return List of RevisionTopicResponse objects for today's revisions
     */
    public List<RevisionTopicResponse> getTodayRevisions(String userId) {
        log.debug("Fetching today's revision topics for user: {}", userId);

        LocalDate today = LocalDate.now();
        List<Topic> todayTopics = topicRepository.findByNextRevision(today);

        return todayTopics.stream()
                .map(this::buildRevisionTopicResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get comprehensive dashboard statistics for a user.
     * 
     * @param userId the user's ID
     * @return object containing all dashboard metrics
     */
    public DashboardStats getDashboardStats(String userId) {
        log.debug("Computing dashboard statistics for user: {}", userId);

        // Get all sections for user
        List<Section> sections = sectionRepository.findByUserId(userId);
        List<String> sectionIds = sections.stream().map(Section::getId).collect(Collectors.toList());

        // Count topics
        long totalTopics = 0;
        long completedTopics = 0;
        long todayRevisions = topicRepository.findByNextRevision(LocalDate.now()).size();

        for (String sectionId : sectionIds) {
            List<String> subsectionIds = subsectionRepository.findBySectionId(sectionId)
                    .stream()
                    .map(Subsection::getId)
                    .collect(Collectors.toList());

            for (String subsectionId : subsectionIds) {
                totalTopics += topicRepository.findBySubsectionId(subsectionId).size();
                completedTopics += topicRepository.countBySubsectionIdAndStatus(subsectionId, "COMPLETED");
            }
        }

        double completionPercentage = totalTopics > 0 ? (completedTopics * 100.0) / totalTopics : 0.0;

        return DashboardStats.builder()
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .pendingTopics(totalTopics - completedTopics)
                .completionPercentage(completionPercentage)
                .todayRevisionCount(todayRevisions)
                .totalSections((long) sections.size())
                .build();
    }

    /**
     * Verify that user has access to a section.
     */
    private void verifyAccess(String userId, String sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this section");
        }
    }

    /**
     * Build TopicResponse from Topic entity.
     */
    private TopicResponse buildTopicResponse(Topic topic) {
        return TopicResponse.builder()
                .id(topic.getId())
                .subsectionId(topic.getSubsectionId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .status(topic.getStatus())
                .completedAt(topic.getCompletedAt())
                .revisionDates(topic.getRevisionDates())
                .nextRevision(topic.getNextRevision())
                .isDueForRevisionToday(topic.isRevisionDueToday())
                .createdAt(topic.getCreatedAt())
                .updatedAt(topic.getUpdatedAt())
                .build();
    }

    /**
     * Build RevisionTopicResponse from Topic entity.
     */
    private RevisionTopicResponse buildRevisionTopicResponse(Topic topic) {
        Subsection subsection = subsectionRepository.findById(topic.getSubsectionId())
                .orElse(new Subsection());

        Section section = sectionRepository.findById(subsection.getSectionId())
                .orElse(new Section());

        return RevisionTopicResponse.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .sectionId(section.getId())
                .subsectionTitle(subsection.getTitle())
                .sectionTitle(section.getTitle())
                .revisionDate(topic.getNextRevision())
                .build();
    }

    /**
     * Inner class for dashboard statistics.
     */
    @lombok.Data
    @lombok.Builder
    public static class DashboardStats {
        private Long totalTopics;
        private Long completedTopics;
        private Long pendingTopics;
        private Double completionPercentage;
        private Long todayRevisionCount;
        private Long totalSections;
    }
}
