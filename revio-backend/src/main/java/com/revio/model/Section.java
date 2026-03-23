package com.revio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Section entity representing a major study section.
 * Example: Data Structures and Algorithms, Web Development, etc.
 * Stored in MongoDB collection 'sections'.
 * 
 * Fields:
 * - id: Unique identifier (MongoDB ObjectId)
 * - userId: Reference to the user who owns this section
 * - title: Name of the section
 * - description: Optional description
 * - createdAt: Creation timestamp
 * - updatedAt: Last update timestamp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sections")
public class Section {

    @Id
    private String id;

    private String userId;

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
