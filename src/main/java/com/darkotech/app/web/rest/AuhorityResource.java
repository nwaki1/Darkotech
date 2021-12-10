package com.darkotech.app.web.rest;

import com.darkotech.app.domain.Auhority;
import com.darkotech.app.repository.AuhorityRepository;
import com.darkotech.app.service.AuhorityService;
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
 * REST controller for managing {@link com.darkotech.app.domain.Auhority}.
 */
@RestController
@RequestMapping("/api")
public class AuhorityResource {

    private final Logger log = LoggerFactory.getLogger(AuhorityResource.class);

    private static final String ENTITY_NAME = "auhority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuhorityService auhorityService;

    private final AuhorityRepository auhorityRepository;

    public AuhorityResource(AuhorityService auhorityService, AuhorityRepository auhorityRepository) {
        this.auhorityService = auhorityService;
        this.auhorityRepository = auhorityRepository;
    }

    /**
     * {@code POST  /auhorities} : Create a new auhority.
     *
     * @param auhority the auhority to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auhority, or with status {@code 400 (Bad Request)} if the auhority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auhorities")
    public ResponseEntity<Auhority> createAuhority(@RequestBody Auhority auhority) throws URISyntaxException {
        log.debug("REST request to save Auhority : {}", auhority);
        if (auhority.getId() != null) {
            throw new BadRequestAlertException("A new auhority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Auhority result = auhorityService.save(auhority);
        return ResponseEntity
            .created(new URI("/api/auhorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auhorities/:id} : Updates an existing auhority.
     *
     * @param id the id of the auhority to save.
     * @param auhority the auhority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auhority,
     * or with status {@code 400 (Bad Request)} if the auhority is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auhority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auhorities/{id}")
    public ResponseEntity<Auhority> updateAuhority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Auhority auhority
    ) throws URISyntaxException {
        log.debug("REST request to update Auhority : {}, {}", id, auhority);
        if (auhority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auhority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auhorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Auhority result = auhorityService.save(auhority);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auhority.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /auhorities/:id} : Partial updates given fields of an existing auhority, field will ignore if it is null
     *
     * @param id the id of the auhority to save.
     * @param auhority the auhority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auhority,
     * or with status {@code 400 (Bad Request)} if the auhority is not valid,
     * or with status {@code 404 (Not Found)} if the auhority is not found,
     * or with status {@code 500 (Internal Server Error)} if the auhority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/auhorities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Auhority> partialUpdateAuhority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Auhority auhority
    ) throws URISyntaxException {
        log.debug("REST request to partial update Auhority partially : {}, {}", id, auhority);
        if (auhority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auhority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auhorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Auhority> result = auhorityService.partialUpdate(auhority);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auhority.getId().toString())
        );
    }

    /**
     * {@code GET  /auhorities} : get all the auhorities.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auhorities in body.
     */
    @GetMapping("/auhorities")
    public List<Auhority> getAllAuhorities(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Auhorities");
        return auhorityService.findAll();
    }

    /**
     * {@code GET  /auhorities/:id} : get the "id" auhority.
     *
     * @param id the id of the auhority to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auhority, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auhorities/{id}")
    public ResponseEntity<Auhority> getAuhority(@PathVariable Long id) {
        log.debug("REST request to get Auhority : {}", id);
        Optional<Auhority> auhority = auhorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auhority);
    }

    /**
     * {@code DELETE  /auhorities/:id} : delete the "id" auhority.
     *
     * @param id the id of the auhority to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auhorities/{id}")
    public ResponseEntity<Void> deleteAuhority(@PathVariable Long id) {
        log.debug("REST request to delete Auhority : {}", id);
        auhorityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
