package com.darkotech.app.service;

import com.darkotech.app.domain.Absence;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Absence}.
 */
public interface AbsenceService {
    /**
     * Save a absence.
     *
     * @param absence the entity to save.
     * @return the persisted entity.
     */
    Absence save(Absence absence);

    /**
     * Partially updates a absence.
     *
     * @param absence the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Absence> partialUpdate(Absence absence);

    /**
     * Get all the absences.
     *
     * @return the list of entities.
     */
    List<Absence> findAll();

    /**
     * Get the "id" absence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Absence> findOne(Long id);

    /**
     * Delete the "id" absence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
