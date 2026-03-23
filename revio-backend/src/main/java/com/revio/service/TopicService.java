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
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubsectionRepository subsectionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    public TopicResponse createTopic(String subsectionId, TopicRequest topicRequest) {
        Subsection subsection = subsectionRepository.findById(subsectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subsection not found with ID: " + subsectionId));

        Topic topic = Topic.builder()
                .subsectionId(subsection.getId())
                .title(topicRequest.getTitle())
                .description(topicRequest.getDescription())
                .status("PENDING")
                .revisionDates(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return buildTopicResponse(topicRepository.save(topic));
    }

    public TopicResponse createTopic(String userId, String sectionId, String subsectionId, TopicRequest topicRequest) {
        verifySectionAccess(userId, sectionId);

        Subsection subsection = subsectionRepository.findByIdAndSectionId(subsectionId, sectionId);
        if (subsection == null) {
            throw new ResourceNotFoundException("Subsection not found");
        }

        Topic topic = Topic.builder()
                .subsectionId(subsectionId)
                .title(topicRequest.getTitle())
                .description(topicRequest.getDescription())
                .status("PENDING")
                .revisionDates(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return buildTopicResponse(topicRepository.save(topic));
    }

    public TopicResponse getTopicById(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));
        return buildTopicResponse(topic);
    }

    public TopicResponse getTopicById(String userId, String sectionId, String topicId) {
        verifySectionAccess(userId, sectionId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        return buildTopicResponse(topic);
    }

    public List<TopicResponse> getTopicsBySubsection(String userId, String sectionId, String subsectionId) {
        verifySectionAccess(userId, sectionId);
        return topicRepository.findBySubsectionId(subsectionId)
                .stream()
                .map(this::buildTopicResponse)
                .collect(Collectors.toList());
    }

    public TopicResponse updateTopic(String topicId, TopicRequest topicRequest) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));

        topic.setTitle(topicRequest.getTitle());
        topic.setDescription(topicRequest.getDescription());
        topic.setUpdatedAt(LocalDateTime.now());
        return buildTopicResponse(topicRepository.save(topic));
    }

    public TopicResponse updateTopic(String userId, String sectionId, String topicId, TopicRequest topicRequest) {
        verifySectionAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        topic.setTitle(topicRequest.getTitle());
        topic.setDescription(topicRequest.getDescription());
        topic.setUpdatedAt(LocalDateTime.now());
        return buildTopicResponse(topicRepository.save(topic));
    }

    public void deleteTopic(String topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic not found with ID: " + topicId);
        }
        topicRepository.deleteById(topicId);
    }

    public void deleteTopic(String userId, String sectionId, String topicId) {
        verifySectionAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        topicRepository.deleteById(topic.getId());
    }

    public TopicResponse markTopicComplete(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));
        applyCompletionSchedule(topic);
        return buildTopicResponse(topicRepository.save(topic));
    }

    public TopicResponse completeTopicAndScheduleRevision(String userId, String sectionId, String topicId) {
        verifySectionAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        applyCompletionSchedule(topic);
        return buildTopicResponse(topicRepository.save(topic));
    }

    public TopicResponse updateNextRevision(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Topic not found with ID: " + topicId));
        advanceRevision(topic);
        return buildTopicResponse(topicRepository.save(topic));
    }

    public TopicResponse completeRevisionForTopic(String userId, String sectionId, String topicId) {
        verifySectionAccess(userId, sectionId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        advanceRevision(topic);
        return buildTopicResponse(topicRepository.save(topic));
    }

    public List<RevisionTopicResponse> getTodayRevisionTopics() {
        return topicRepository.findByNextRevision(LocalDate.now())
                .stream()
                .map(this::buildRevisionTopicResponse)
                .collect(Collectors.toList());
    }

    public List<RevisionTopicResponse> getTodayRevisions(String userId) {
        List<String> sectionIds = sectionRepository.findByUserId(userId)
                .stream()
                .map(Section::getId)
                .collect(Collectors.toList());

        if (sectionIds.isEmpty()) {
            return List.of();
        }

        return topicRepository.findByNextRevision(LocalDate.now())
                .stream()
                .filter(topic -> belongsToUserSections(topic.getSubsectionId(), sectionIds))
                .map(this::buildRevisionTopicResponse)
                .collect(Collectors.toList());
    }

    public DashboardStats getDashboardStats() {
        long totalTopics = topicRepository.count();
        long completedTopics = topicRepository.findByStatus("COMPLETED").size();
        long todayRevisionCount = topicRepository.findByNextRevision(LocalDate.now()).size();

        return DashboardStats.builder()
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .pendingTopics(totalTopics - completedTopics)
                .completionPercentage(totalTopics > 0 ? (completedTopics * 100.0) / totalTopics : 0.0)
                .todayRevisionCount(todayRevisionCount)
                .totalSections(sectionRepository.count())
                .build();
    }

    public DashboardStats getDashboardStats(String userId) {
        List<Section> sections = sectionRepository.findByUserId(userId);
        List<String> sectionIds = sections.stream()
                .map(Section::getId)
                .collect(Collectors.toList());

        long totalTopics = 0;
        long completedTopics = 0;

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

        long todayRevisionCount = getTodayRevisions(userId).size();

        return DashboardStats.builder()
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .pendingTopics(totalTopics - completedTopics)
                .completionPercentage(totalTopics > 0 ? (completedTopics * 100.0) / totalTopics : 0.0)
                .todayRevisionCount(todayRevisionCount)
                .totalSections((long) sections.size())
                .build();
    }

    private void verifySectionAccess(String userId, String sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        if (!section.getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this section");
        }
    }

    private boolean belongsToUserSections(String subsectionId, List<String> sectionIds) {
        return subsectionRepository.findById(subsectionId)
                .map(Subsection::getSectionId)
                .map(sectionIds::contains)
                .orElse(false);
    }

    private void applyCompletionSchedule(Topic topic) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        List<LocalDate> revisionDates = new ArrayList<>();
        revisionDates.add(today);
        revisionDates.add(today.plusDays(1));
        revisionDates.add(today.plusDays(4));
        revisionDates.add(today.plusDays(11));

        topic.setStatus("COMPLETED");
        topic.setCompletedAt(now);
        topic.setRevisionDates(revisionDates);
        topic.setNextRevision(today);
        topic.setUpdatedAt(now);
    }

    private void advanceRevision(Topic topic) {
        List<LocalDate> revisionDates = topic.getRevisionDates();
        if (revisionDates == null || revisionDates.isEmpty()) {
            throw new ResourceNotFoundException("Topic has no scheduled revisions");
        }

        LocalDate currentNextRevision = topic.getNextRevision();
        int currentIndex = revisionDates.indexOf(currentNextRevision);

        if (currentIndex >= 0 && currentIndex < revisionDates.size() - 1) {
            topic.setNextRevision(revisionDates.get(currentIndex + 1));
        } else {
            topic.setNextRevision(null);
            topic.setStatus("FULLY_REVISED");
        }

        topic.setUpdatedAt(LocalDateTime.now());
    }

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

    private RevisionTopicResponse buildRevisionTopicResponse(Topic topic) {
        Subsection subsection = subsectionRepository.findById(topic.getSubsectionId())
                .orElse(null);
        Section section = subsection == null
                ? null
                : sectionRepository.findById(subsection.getSectionId()).orElse(null);

        return RevisionTopicResponse.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .sectionId(section != null ? section.getId() : null)
                .subsectionTitle(subsection != null ? subsection.getTitle() : null)
                .sectionTitle(section != null ? section.getTitle() : null)
                .revisionDate(topic.getNextRevision())
                .build();
    }

    @Data
    @Builder
    public static class DashboardStats {
        private Long totalTopics;
        private Long completedTopics;
        private Long pendingTopics;
        private Double completionPercentage;
        private Long todayRevisionCount;
        private Long totalSections;
    }
}
