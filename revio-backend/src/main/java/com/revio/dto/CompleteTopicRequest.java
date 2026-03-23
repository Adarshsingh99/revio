package com.revio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for marking topic as complete.
 * Triggers revision date generation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompleteTopicRequest {
    
    private String topicId;
    // Optional: in case we need to track revision round
    private Integer revisionRound;
}
