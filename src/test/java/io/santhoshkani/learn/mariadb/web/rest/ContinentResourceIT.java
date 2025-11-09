package io.santhoshkani.learn.mariadb.web.rest;

import static io.santhoshkani.learn.mariadb.domain.ContinentAsserts.*;
import static io.santhoshkani.learn.mariadb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.santhoshkani.learn.mariadb.IntegrationTest;
import io.santhoshkani.learn.mariadb.domain.Continent;
import io.santhoshkani.learn.mariadb.repository.ContinentRepository;
import io.santhoshkani.learn.mariadb.service.dto.ContinentDTO;
import io.santhoshkani.learn.mariadb.service.mapper.ContinentMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContinentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContinentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/continents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContinentRepository continentRepository;

    @Autowired
    private ContinentMapper continentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContinentMockMvc;

    private Continent continent;

    private Continent insertedContinent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Continent createEntity() {
        return new Continent().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Continent createUpdatedEntity() {
        return new Continent().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        continent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContinent != null) {
            continentRepository.delete(insertedContinent);
            insertedContinent = null;
        }
    }

    @Test
    @Transactional
    void createContinent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);
        var returnedContinentDTO = om.readValue(
            restContinentMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(continentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContinentDTO.class
        );

        // Validate the Continent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContinent = continentMapper.toEntity(returnedContinentDTO);
        assertContinentUpdatableFieldsEquals(returnedContinent, getPersistedContinent(returnedContinent));

        insertedContinent = returnedContinent;
    }

    @Test
    @Transactional
    void createContinentWithExistingId() throws Exception {
        // Create the Continent with an existing ID
        continent.setId(1L);
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContinentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(continentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        continent.setName(null);

        // Create the Continent, which fails.
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        restContinentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(continentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContinents() throws Exception {
        // Initialize the database
        insertedContinent = continentRepository.saveAndFlush(continent);

        // Get all the continentList
        restContinentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(continent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getContinent() throws Exception {
        // Initialize the database
        insertedContinent = continentRepository.saveAndFlush(continent);

        // Get the continent
        restContinentMockMvc
            .perform(get(ENTITY_API_URL_ID, continent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(continent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingContinent() throws Exception {
        // Get the continent
        restContinentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContinent() throws Exception {
        // Initialize the database
        insertedContinent = continentRepository.saveAndFlush(continent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the continent
        Continent updatedContinent = continentRepository.findById(continent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContinent are not directly saved in db
        em.detach(updatedContinent);
        updatedContinent.name(UPDATED_NAME);
        ContinentDTO continentDTO = continentMapper.toDto(updatedContinent);

        restContinentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, continentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(continentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContinentToMatchAllProperties(updatedContinent);
    }

    @Test
    @Transactional
    void putNonExistingContinent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        continent.setId(longCount.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, continentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContinent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        continent.setId(longCount.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContinent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        continent.setId(longCount.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(continentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContinentWithPatch() throws Exception {
        // Initialize the database
        insertedContinent = continentRepository.saveAndFlush(continent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the continent using partial update
        Continent partialUpdatedContinent = new Continent();
        partialUpdatedContinent.setId(continent.getId());

        partialUpdatedContinent.name(UPDATED_NAME);

        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContinent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContinent))
            )
            .andExpect(status().isOk());

        // Validate the Continent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContinentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContinent, continent),
            getPersistedContinent(continent)
        );
    }

    @Test
    @Transactional
    void fullUpdateContinentWithPatch() throws Exception {
        // Initialize the database
        insertedContinent = continentRepository.saveAndFlush(continent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the continent using partial update
        Continent partialUpdatedContinent = new Continent();
        partialUpdatedContinent.setId(continent.getId());

        partialUpdatedContinent.name(UPDATED_NAME);

        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContinent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContinent))
            )
            .andExpect(status().isOk());

        // Validate the Continent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContinentUpdatableFieldsEquals(partialUpdatedContinent, getPersistedContinent(partialUpdatedContinent));
    }

    @Test
    @Transactional
    void patchNonExistingContinent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        continent.setId(longCount.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, continentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContinent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        continent.setId(longCount.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContinent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        continent.setId(longCount.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(continentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Continent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContinent() throws Exception {
        // Initialize the database
        insertedContinent = continentRepository.saveAndFlush(continent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the continent
        restContinentMockMvc
            .perform(delete(ENTITY_API_URL_ID, continent.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return continentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Continent getPersistedContinent(Continent continent) {
        return continentRepository.findById(continent.getId()).orElseThrow();
    }

    protected void assertPersistedContinentToMatchAllProperties(Continent expectedContinent) {
        assertContinentAllPropertiesEquals(expectedContinent, getPersistedContinent(expectedContinent));
    }

    protected void assertPersistedContinentToMatchUpdatableProperties(Continent expectedContinent) {
        assertContinentAllUpdatablePropertiesEquals(expectedContinent, getPersistedContinent(expectedContinent));
    }
}
