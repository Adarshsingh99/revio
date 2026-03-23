package com.revio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Topic entity representing a topic within a subsection.
 * Example: Subsection "Arrays" -> Topics "Array Operations", "Binary Search", etc.
 * Includes smart revision scheduling system.
 * Stored in MongoDB collection 'topics'.
 * 
 * Revision System:
 * When a topic is marked as complete, revision dates are auto-generated:
 * - Day 1: Completion day
 * - Day 2: +1 day after completion
 * - Day 3: +3 days after Day 2
 * - Day 4: +7 days after Day 3
 * 
 * Fields:
 * - id: Unique identifier (MongoDB ObjectId)
 * - subsectionId: Reference to the parent subsection
 * - title: Topic name
 * - description: Optional topic description
 * - status: PENDING or COMPLETED
 * - completedAt: When the topic was first marked complete
 * - revisionDates: List of scheduled revision dates
 * - nextRevision: The next revision date to appear in dashboard
 * - createdAt: Creation timestamp
 * - updatedAt: Last update timestamp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "topics")
public class Topic {

    @Id
    private String id;

    private String subsectionId;

    private String title;

    private String description;

    // Status: PENDING, COMPLETED, REVISION
    private String status;

    private LocalDateTime completedAt;

    // List of revision dates generated when topic is marked complete
    private List<LocalDate> revisionDates;

    // Next revision date to display in dashboard
    private LocalDate nextRevision;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Initialize timestamps before persisting
     */
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDING";
        }
    }

    /**
     * Update timestamp before updating
     */
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Check if topic needs revision today
     * @return true if nextRevision is today, false otherwise
     */
    public boolean isRevisionDueToday() {
        if (nextRevision == null) {
            return false;
        }
        return nextRevision.equals(LocalDate.now());
    }
}
