package com.revio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

/**
 * User entity representing a user in the Revio system.
 * Stored in MongoDB collection 'users'.
 * 
 * Fields:
 * - id: Unique identifier (MongoDB ObjectId)
 * - name: User's full name
 * - email: User's email (unique)
 * - password: Encrypted password
 * - createdAt: Account creation timestamp
 * - updatedAt: Last update timestamp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Called before saving to set timestamps
     */
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Called before updating to set updated timestamp
     */
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
