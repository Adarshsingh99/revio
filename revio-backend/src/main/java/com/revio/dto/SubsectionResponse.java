package com.revio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for subsection response.
 * Includes metadata for frontend display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubsectionResponse {

    private String id;
    private String sectionId;
    private String title;
    private String description;
    private Long topicCount;
    private Long completedTopics;
    private Long pendingTopics;
    private Double progressPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
