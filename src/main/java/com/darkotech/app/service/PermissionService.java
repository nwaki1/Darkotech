package com.darkotech.app.service;

import com.darkotech.app.domain.Permission;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Permission}.
 */
public interface PermissionService {
    /**
     * Save a permission.
     *
     * @param permission the entity to save.
     * @return the persisted entity.
     */
    Permission save(Permission permission);

    /**
     * Partially updates a permission.
     *
     * @param permission the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Permission> partialUpdate(Permission permission);

    /**
     * Get all the permissions.
     *
     * @return the list of entities.
     */
    List<Permission> findAll();

    /**
     * Get the "id" permission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Permission> findOne(Long id);

    /**
     * Delete the "id" permission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
