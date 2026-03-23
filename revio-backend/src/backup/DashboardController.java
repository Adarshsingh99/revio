/*
package com.revio.controller;

import com.revio.dto.DashboardResponse;
import com.revio.dto.RevisionTopicResponse;
import com.revio.service.SectionService;
import com.revio.service.TopicService;
import com.revio.service.TopicService.DashboardStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dashboard Controller for comprehensive user statistics.
 * 
 * Endpoints:
 * - GET /dashboard - Get complete dashboard with all statistics
 * 
 * Requires authentication (JWT token).
 */
@RestController
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private SectionService sectionService;

    /**
     * Get comprehensive dashboard statistics for the user.
     * 
     * Returns:
     * - Total topics, completed topics, pending topics
     * - Completion percentage
     * - Today's revision count and list
     * - Total sections and subsections
     * - Breakdown by section
     * 
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with DashboardResponse
     */
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        log.info("Fetching dashboard for user: {}", userId);

        try {
            // Get overall statistics
            DashboardStats stats = topicService.getDashboardStats(userId);

            // Get today's revisions
            List<RevisionTopicResponse> todayRevisions = topicService.getTodayRevisions(userId);

            // Get all sections
            var sections = sectionService.getSectionsByUserId(userId);

            // Build response
            DashboardResponse dashboard = DashboardResponse.builder()
                    .totalTopics(stats.getTotalTopics())
                    .completedTopics(stats.getCompletedTopics())
                    .pendingTopics(stats.getPendingTopics())
                    .completionPercentage(stats.getCompletionPercentage())
                    .todayRevisionCount((long) todayRevisions.size())
                    .todayRevisions(todayRevisions)
                    .totalSections(stats.getTotalSections())
                    .totalSubsections(0L) // Can be computed from sections
                    .build();

            log.info("Dashboard fetched successfully for user: {}", userId);
            return new ResponseEntity<>(dashboard, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error fetching dashboard for user: {}", userId, ex);
            throw ex;
        }
    }

    /**
     * Health check endpoint for dashboard.
     * 
     * @return simple status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Dashboard API is running!", HttpStatus.OK);
    }
}
*/
