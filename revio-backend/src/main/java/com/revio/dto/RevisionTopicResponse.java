package com.revio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * DTO for today's revision topic.
 * Lightweight response used in revision list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevisionTopicResponse {

    private String id;
    private String title;
    private String sectionId;
    private String subsectionTitle;
    private String sectionTitle;
    private LocalDate revisionDate;
}
