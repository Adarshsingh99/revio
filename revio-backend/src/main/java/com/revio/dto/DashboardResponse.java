package com.revio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for dashboard response.
 * Contains comprehensive statistics and today's revision list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    // Overall statistics
    private Long totalTopics;
    private Long completedTopics;
    private Long pendingTopics;
    private Double completionPercentage;
    
    // Revision statistics
    private Long todayRevisionCount;
    private List<RevisionTopicResponse> todayRevisions;
    
    // Study statistics
    private Long totalSections;
    private Long totalSubsections;
    
    // Detailed breakdown by section
    private List<SectionStats> sectionStats;
}

/**
 * DTO for section statistics on dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SectionStats {
    
    private String sectionId;
    private String sectionTitle;
    private Long totalTopics;
    private Long completedTopics;
    private Long pendingTopics;
    private Double progressPercentage;
}
