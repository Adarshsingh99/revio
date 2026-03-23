package com.revio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for topic response.
 * Includes all topic information and revision details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicResponse {

    private String id;
    private String subsectionId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime completedAt;
    private List<LocalDate> revisionDates;
    private LocalDate nextRevision;
    private boolean isDueForRevisionToday;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
