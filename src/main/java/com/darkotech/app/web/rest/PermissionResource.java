package com.darkotech.app.web.rest;

import com.darkotech.app.domain.Permission;
import com.darkotech.app.repository.PermissionRepository;
import com.darkotech.app.service.PermissionService;
import com.darkotech.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.darkotech.app.domain.Permission}.
 */
@RestController
@RequestMapping("/api")
public class PermissionResource {

    private final Logger log = LoggerFactory.getLogger(PermissionResource.class);

    private static final String ENTITY_NAME = "permission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionService permissionService;

    private final PermissionRepository permissionRepository;

    public PermissionResource(PermissionService permissionService, PermissionRepository permissionRepository) {
        this.permissionService = permissionService;
        this.permissionRepository = permissionRepository;
    }

    /**
     * {@code POST  /permissions} : Create a new permission.
     *
     * @param permission the permission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permission, or with status {@code 400 (Bad Request)} if the permission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws URISyntaxException {
        log.debug("REST request to save Permission : {}", permission);
        if (permission.getId() != null) {
            throw new BadRequestAlertException("A new permission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Permission result = permissionService.save(permission);
        return ResponseEntity
            .created(new URI("/api/permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permissions/:id} : Updates an existing permission.
     *
     * @param id the id of the permission to save.
     * @param permission the permission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permission,
     * or with status {@code 400 (Bad Request)} if the permission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions/{id}")
    public ResponseEntity<Permission> updatePermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Permission permission
    ) throws URISyntaxException {
        log.debug("REST request to update Permission : {}, {}", id, permission);
        if (permission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Permission result = permissionService.save(permission);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permission.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permissions/:id} : Partial updates given fields of an existing permission, field will ignore if it is null
     *
     * @param id the id of the permission to save.
     * @param permission the permission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permission,
     * or with status {@code 400 (Bad Request)} if the permission is not valid,
     * or with status {@code 404 (Not Found)} if the permission is not found,
     * or with status {@code 500 (Internal Server Error)} if the permission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Permission> partialUpdatePermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Permission permission
    ) throws URISyntaxException {
        log.debug("REST request to partial update Permission partially : {}, {}", id, permission);
        if (permission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Permission> result = permissionService.partialUpdate(permission);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permission.getId().toString())
        );
    }

    /**
     * {@code GET  /permissions} : get all the permissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissions in body.
     */
    @GetMapping("/permissions")
    public List<Permission> getAllPermissions() {
        log.debug("REST request to get all Permissions");
        return permissionService.findAll();
    }

    /**
     * {@code GET  /permissions/:id} : get the "id" permission.
     *
     * @param id the id of the permission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permission> getPermission(@PathVariable Long id) {
        log.debug("REST request to get Permission : {}", id);
        Optional<Permission> permission = permissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permission);
    }

    /**
     * {@code DELETE  /permissions/:id} : delete the "id" permission.
     *
     * @param id the id of the permission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        log.debug("REST request to delete Permission : {}", id);
        permissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
