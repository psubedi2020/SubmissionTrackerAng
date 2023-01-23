package com.devguru.submission.service;

import com.devguru.submission.domain.Candidate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Candidate}.
 */
public interface CandidateService {
    /**
     * Save a candidate.
     *
     * @param candidate the entity to save.
     * @return the persisted entity.
     */
    Candidate save(Candidate candidate);

    /**
     * Updates a candidate.
     *
     * @param candidate the entity to update.
     * @return the persisted entity.
     */
    Candidate update(Candidate candidate);

    /**
     * Partially updates a candidate.
     *
     * @param candidate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Candidate> partialUpdate(Candidate candidate);

    /**
     * Get all the candidates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Candidate> findAll(Pageable pageable);

    /**
     * Get the "id" candidate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Candidate> findOne(Long id);

    /**
     * Delete the "id" candidate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
