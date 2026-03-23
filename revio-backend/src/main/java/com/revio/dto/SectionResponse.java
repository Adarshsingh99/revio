package com.revio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for section response.
 * Includes metadata for frontend display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponse {

    private String id;
    private String userId;
    private String title;
    private String description;
    private Long subsectionCount;
    private Long totalTopics;
    private Long completedTopics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
