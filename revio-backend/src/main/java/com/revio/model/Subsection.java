package com.revio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Subsection entity representing a subsection within a Section.
 * Example: Section "DSA" -> Subsections "Arrays", "Graphs", "Trees", etc.
 * Stored in MongoDB collection 'subsections'.
 * 
 * Fields:
 * - id: Unique identifier (MongoDB ObjectId)
 * - sectionId: Reference to the parent section
 * - title: Name of the subsection
 * - description: Optional description
 * - createdAt: Creation timestamp
 * - updatedAt: Last update timestamp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "subsections")
public class Subsection {

    @Id
    private String id;

    private String sectionId;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Initialize timestamps before persisting
     */
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update timestamp before updating
     */
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
