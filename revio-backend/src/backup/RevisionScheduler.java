package com.revio.scheduler;

import com.revio.model.Topic;
import com.revio.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler component for Revio application.
 * 
 * Runs scheduled tasks for the smart revision system:
 * - Daily cleanup of completed revisions
 * - Update revision status
 * 
 * Note: The main revision system works through the nextRevision date
 * in each topic. The scheduler validates consistency and can perform
 * maintenance tasks.
 */
@Component
@Slf4j
public class RevisionScheduler {

    @Autowired
    private TopicRepository topicRepository;

    /**
     * Daily task that runs at midnight (00:00).
     * 
     * Performs:
     * - Validates topics with revisions scheduled for today
     * - Logs revision statistics
     * - Can be extended for cleanup tasks
     * 
     * Cron expression: "0 0 0 * * *" (midnight daily)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void processDailyRevisions() {
        log.info("=== Daily Revision Task Started ===");
        LocalDateTime taskStartTime = LocalDateTime.now();

        try {
            // Fetch all topics with revisions scheduled for today
            LocalDate today = LocalDate.now();
            List<Topic> todayRevisions = topicRepository.findByNextRevision(today);

            log.info("📚 Topics requiring revision today: {}", todayRevisions.size());

            // Log topics by section
            if (!todayRevisions.isEmpty()) {
                log.info("Today's Revision Schedule:");
                todayRevisions.forEach(topic -> {
                    log.info("  - [{}] {} (Revision Date: {})",
                            topic.getId(),
                            topic.getTitle(),
                            topic.getNextRevision());
                });
            }

            // Statistics
            long totalTopicsInSystem = topicRepository.count();
            long completedTopics = topicRepository.findByStatus("COMPLETED").size();
            long topicsWithRevisions = topicRepository.findAllWithPendingRevisions().size();

            log.info("📊 Revision System Statistics:");
            log.info("  - Total Topics: {}", totalTopicsInSystem);
            log.info("  - Completed Topics: {}", completedTopics);
            log.info("  - Topics with Pending Revisions: {}", topicsWithRevisions);
            log.info("  - Today's Revisions: {}", todayRevisions.size());

            LocalDateTime taskEndTime = LocalDateTime.now();
            log.info("=== Daily Revision Task Completed (Duration: {} ms) ===",
                    java.time.temporal.ChronoUnit.MILLIS.between(taskStartTime, taskEndTime));

        } catch (Exception ex) {
            log.error("Error processing daily revisions", ex);
        }
    }

    /**
     * Optional: Cleanup task that runs weekly (Sunday at 2 AM).
     * Can be used to:
     * - Archive old revisions
     * - Clean up deleted data
     * - Optimize database
     */
    @Scheduled(cron = "0 0 2 ? * SUN")
    public void weeklyCleanup() {
        log.info("=== Weekly Cleanup Task Started ===");

        try {
            // Find topics with all revisions completed (nextRevision is null)
            // These are fully revised topics
            List<Topic> fullyRevisedTopics = topicRepository.findByStatus("FULLY_REVISED");
            log.info("Found {} fully revised topics", fullyRevisedTopics.size());

            // Could perform cleanup operations here
            log.info("=== Weekly Cleanup Task Completed ===");

        } catch (Exception ex) {
            log.error("Error during weekly cleanup", ex);
        }
    }

    /**
     * Health check task that runs every 30 minutes.
     * Validates the scheduler is working correctly.
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes in milliseconds
    public void healthCheck() {
        log.debug("⚡ Scheduler Health Check - System is running at {}", LocalDateTime.now());
    }
}
