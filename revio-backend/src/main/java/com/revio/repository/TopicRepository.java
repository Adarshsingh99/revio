package com.revio.repository;

import com.revio.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Topic entity.
 * Provides CRUD operations and custom queries for Topic documents in MongoDB.
 * Includes specialized queries for the smart revision system.
 */
@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {

    /**
     * Find all topics belonging to a specific subsection.
     * @param subsectionId the subsection's ID
     * @return List of topics in the subsection
     */
    List<Topic> findBySubsectionId(String subsectionId);

    /**
     * Find all topics with a specific status.
     * @param status the status (PENDING, COMPLETED, REVISION)
     * @return List of topics with the given status
     */
    List<Topic> findByStatus(String status);

    /**
     * Find topics where nextRevision equals today's date.
     * Used to fetch today's revision list.
     * @param date the date to check (typically LocalDate.now())
     * @return List of topics that need revision today
     */
    List<Topic> findByNextRevision(LocalDate date);

    /**
     * Find all topics that need revision (have a nextRevision date set).
     * @return List of topics with pending revisions
     */
    @Query("{ 'nextRevision': { $ne: null } }")
    List<Topic> findAllWithPendingRevisions();

    /**
     * Find all topics in a subsection with COMPLETED status.
     * @param subsectionId the subsection ID
     * @return List of completed topics
     */
    List<Topic> findBySubsectionIdAndStatus(String subsectionId, String status);

    /**
     * Count topics with PENDING status in a subsection.
     * @param subsectionId the subsection ID
     * @return count of pending topics
     */
    long countBySubsectionIdAndStatus(String subsectionId, String status);
}
