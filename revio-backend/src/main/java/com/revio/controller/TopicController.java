package com.revio.controller;

import com.revio.dto.RevisionTopicResponse;
import com.revio.dto.TopicRequest;
import com.revio.dto.TopicResponse;
import com.revio.service.TopicService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/sections/{sectionId}/subsections/{subsectionId}/topics")
    public ResponseEntity<List<TopicResponse>> getTopics(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching topics for subsection {} by user: {}", subsectionId, userId);

        return new ResponseEntity<>(topicService.getTopicsBySubsection(userId, sectionId, subsectionId), HttpStatus.OK);
    }

    @GetMapping("/sections/{sectionId}/subsections/{subsectionId}/topics/{topicId}")
    public ResponseEntity<TopicResponse> getTopicById(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId,
            @PathVariable String topicId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching topic {} by user: {}", topicId, userId);

        return new ResponseEntity<>(topicService.getTopicById(userId, sectionId, topicId), HttpStatus.OK);
    }

    @PostMapping("/sections/{sectionId}/subsections/{subsectionId}/topics")
    public ResponseEntity<TopicResponse> createTopic(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId,
            @Valid @RequestBody TopicRequest topicRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Creating topic for subsection {} by user: {}", subsectionId, userId);

        return new ResponseEntity<>(
                topicService.createTopic(userId, sectionId, subsectionId, topicRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/sections/{sectionId}/subsections/{subsectionId}/topics/{topicId}")
    public ResponseEntity<TopicResponse> updateTopic(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId,
            @PathVariable String topicId,
            @Valid @RequestBody TopicRequest topicRequest) {
        String userId = (String) authentication.getPrincipal();
        log.info("Updating topic {} by user: {}", topicId, userId);

        return new ResponseEntity<>(topicService.updateTopic(userId, sectionId, topicId, topicRequest), HttpStatus.OK);
    }

    @DeleteMapping("/sections/{sectionId}/subsections/{subsectionId}/topics/{topicId}")
    public ResponseEntity<String> deleteTopic(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String subsectionId,
            @PathVariable String topicId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Deleting topic {} by user: {}", topicId, userId);

        topicService.deleteTopic(userId, sectionId, topicId);
        return new ResponseEntity<>("Topic deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/sections/{sectionId}/topics/{topicId}/complete")
    public ResponseEntity<TopicResponse> completeTopic(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String topicId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Marking topic {} as complete and scheduling revisions by user: {}", topicId, userId);

        return new ResponseEntity<>(topicService.completeTopicAndScheduleRevision(userId, sectionId, topicId), HttpStatus.OK);
    }

    @PostMapping("/sections/{sectionId}/topics/{topicId}/revise")
    public ResponseEntity<TopicResponse> completeRevision(
            Authentication authentication,
            @PathVariable String sectionId,
            @PathVariable String topicId) {
        String userId = (String) authentication.getPrincipal();
        log.info("Completing revision for topic {} by user: {}", topicId, userId);

        return new ResponseEntity<>(topicService.completeRevisionForTopic(userId, sectionId, topicId), HttpStatus.OK);
    }

    @GetMapping("/revision/today")
    public ResponseEntity<List<RevisionTopicResponse>> getTodayRevisions(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching today's revision topics for user: {}", userId);

        return new ResponseEntity<>(topicService.getTodayRevisions(userId), HttpStatus.OK);
    }
}
