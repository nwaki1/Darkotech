package com.darkotech.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.darkotech.app.IntegrationTest;
import com.darkotech.app.domain.Auhority;
import com.darkotech.app.repository.AuhorityRepository;
import com.darkotech.app.service.AuhorityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AuhorityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AuhorityResourceIT {

    private static final String DEFAULT_AUHORITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUHORITY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/auhorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuhorityRepository auhorityRepository;

    @Mock
    private AuhorityRepository auhorityRepositoryMock;

    @Mock
    private AuhorityService auhorityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuhorityMockMvc;

    private Auhority auhority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auhority createEntity(EntityManager em) {
        Auhority auhority = new Auhority().auhorityName(DEFAULT_AUHORITY_NAME);
        return auhority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auhority createUpdatedEntity(EntityManager em) {
        Auhority auhority = new Auhority().auhorityName(UPDATED_AUHORITY_NAME);
        return auhority;
    }

    @BeforeEach
    public void initTest() {
        auhority = createEntity(em);
    }

    @Test
    @Transactional
    void createAuhority() throws Exception {
        int databaseSizeBeforeCreate = auhorityRepository.findAll().size();
        // Create the Auhority
        restAuhorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auhority)))
            .andExpect(status().isCreated());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeCreate + 1);
        Auhority testAuhority = auhorityList.get(auhorityList.size() - 1);
        assertThat(testAuhority.getAuhorityName()).isEqualTo(DEFAULT_AUHORITY_NAME);
    }

    @Test
    @Transactional
    void createAuhorityWithExistingId() throws Exception {
        // Create the Auhority with an existing ID
        auhority.setId(1L);

        int databaseSizeBeforeCreate = auhorityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuhorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auhority)))
            .andExpect(status().isBadRequest());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAuhorities() throws Exception {
        // Initialize the database
        auhorityRepository.saveAndFlush(auhority);

        // Get all the auhorityList
        restAuhorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auhority.getId().intValue())))
            .andExpect(jsonPath("$.[*].auhorityName").value(hasItem(DEFAULT_AUHORITY_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuhoritiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(auhorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAuhorityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(auhorityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuhoritiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(auhorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAuhorityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(auhorityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAuhority() throws Exception {
        // Initialize the database
        auhorityRepository.saveAndFlush(auhority);

        // Get the auhority
        restAuhorityMockMvc
            .perform(get(ENTITY_API_URL_ID, auhority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auhority.getId().intValue()))
            .andExpect(jsonPath("$.auhorityName").value(DEFAULT_AUHORITY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAuhority() throws Exception {
        // Get the auhority
        restAuhorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAuhority() throws Exception {
        // Initialize the database
        auhorityRepository.saveAndFlush(auhority);

        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();

        // Update the auhority
        Auhority updatedAuhority = auhorityRepository.findById(auhority.getId()).get();
        // Disconnect from session so that the updates on updatedAuhority are not directly saved in db
        em.detach(updatedAuhority);
        updatedAuhority.auhorityName(UPDATED_AUHORITY_NAME);

        restAuhorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAuhority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAuhority))
            )
            .andExpect(status().isOk());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
        Auhority testAuhority = auhorityList.get(auhorityList.size() - 1);
        assertThat(testAuhority.getAuhorityName()).isEqualTo(UPDATED_AUHORITY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAuhority() throws Exception {
        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();
        auhority.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuhorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auhority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auhority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuhority() throws Exception {
        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();
        auhority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuhorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auhority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuhority() throws Exception {
        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();
        auhority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuhorityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auhority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuhorityWithPatch() throws Exception {
        // Initialize the database
        auhorityRepository.saveAndFlush(auhority);

        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();

        // Update the auhority using partial update
        Auhority partialUpdatedAuhority = new Auhority();
        partialUpdatedAuhority.setId(auhority.getId());

        restAuhorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuhority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuhority))
            )
            .andExpect(status().isOk());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
        Auhority testAuhority = auhorityList.get(auhorityList.size() - 1);
        assertThat(testAuhority.getAuhorityName()).isEqualTo(DEFAULT_AUHORITY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAuhorityWithPatch() throws Exception {
        // Initialize the database
        auhorityRepository.saveAndFlush(auhority);

        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();

        // Update the auhority using partial update
        Auhority partialUpdatedAuhority = new Auhority();
        partialUpdatedAuhority.setId(auhority.getId());

        partialUpdatedAuhority.auhorityName(UPDATED_AUHORITY_NAME);

        restAuhorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuhority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuhority))
            )
            .andExpect(status().isOk());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
        Auhority testAuhority = auhorityList.get(auhorityList.size() - 1);
        assertThat(testAuhority.getAuhorityName()).isEqualTo(UPDATED_AUHORITY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAuhority() throws Exception {
        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();
        auhority.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuhorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auhority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auhority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuhority() throws Exception {
        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();
        auhority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuhorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auhority))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuhority() throws Exception {
        int databaseSizeBeforeUpdate = auhorityRepository.findAll().size();
        auhority.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuhorityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(auhority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Auhority in the database
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuhority() throws Exception {
        // Initialize the database
        auhorityRepository.saveAndFlush(auhority);

        int databaseSizeBeforeDelete = auhorityRepository.findAll().size();

        // Delete the auhority
        restAuhorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, auhority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Auhority> auhorityList = auhorityRepository.findAll();
        assertThat(auhorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
