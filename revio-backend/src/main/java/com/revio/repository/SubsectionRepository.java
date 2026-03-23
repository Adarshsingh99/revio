package com.revio.repository;

import com.revio.model.Subsection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Subsection entity.
 * Provides CRUD operations and custom queries for Subsection documents in MongoDB.
 */
@Repository
public interface SubsectionRepository extends MongoRepository<Subsection, String> {

    /**
     * Find all subsections belonging to a specific section.
     * @param sectionId the section's ID
     * @return List of subsections in the section
     */
    List<Subsection> findBySectionId(String sectionId);

    /**
     * Find a subsection by its ID and section ID.
     * @param id the subsection ID
     * @param sectionId the section ID
     * @return the subsection if it exists
     */
    Subsection findByIdAndSectionId(String id, String sectionId);
}
