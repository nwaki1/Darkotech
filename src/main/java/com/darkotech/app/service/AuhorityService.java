package com.darkotech.app.service;

import com.darkotech.app.domain.Auhority;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Auhority}.
 */
public interface AuhorityService {
    /**
     * Save a auhority.
     *
     * @param auhority the entity to save.
     * @return the persisted entity.
     */
    Auhority save(Auhority auhority);

    /**
     * Partially updates a auhority.
     *
     * @param auhority the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Auhority> partialUpdate(Auhority auhority);

    /**
     * Get all the auhorities.
     *
     * @return the list of entities.
     */
    List<Auhority> findAll();

    /**
     * Get all the auhorities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Auhority> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" auhority.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Auhority> findOne(Long id);

    /**
     * Delete the "id" auhority.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
