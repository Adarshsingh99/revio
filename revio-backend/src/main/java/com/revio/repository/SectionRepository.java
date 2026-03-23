package com.revio.repository;

import com.revio.model.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Section entity.
 * Provides CRUD operations and custom queries for Section documents in MongoDB.
 */
@Repository
public interface SectionRepository extends MongoRepository<Section, String> {

    /**
     * Find all sections belonging to a specific user.
     * @param userId the user's ID
     * @return List of sections owned by the user
     */
    List<Section> findByUserId(String userId);

    /**
     * Find a section by its ID and user ID to verify ownership.
     * @param id the section ID
     * @param userId the user ID
     * @return the section if it exists and belongs to the user
     */
    Section findByIdAndUserId(String id, String userId);
}
