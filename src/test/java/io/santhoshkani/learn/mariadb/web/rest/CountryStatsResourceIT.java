package io.santhoshkani.learn.mariadb.web.rest;

import static io.santhoshkani.learn.mariadb.domain.CountryStatsAsserts.*;
import static io.santhoshkani.learn.mariadb.web.rest.TestUtil.createUpdateProxyForBean;
import static io.santhoshkani.learn.mariadb.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.santhoshkani.learn.mariadb.IntegrationTest;
import io.santhoshkani.learn.mariadb.domain.CountryStats;
import io.santhoshkani.learn.mariadb.repository.CountryStatsRepository;
import io.santhoshkani.learn.mariadb.service.CountryStatsService;
import io.santhoshkani.learn.mariadb.service.dto.CountryStatsDTO;
import io.santhoshkani.learn.mariadb.service.mapper.CountryStatsMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CountryStatsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CountryStatsResourceIT {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Long DEFAULT_POPULATION = 1L;
    private static final Long UPDATED_POPULATION = 2L;

    private static final BigDecimal DEFAULT_GDP = new BigDecimal(1);
    private static final BigDecimal UPDATED_GDP = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/country-stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CountryStatsRepository countryStatsRepository;

    @Mock
    private CountryStatsRepository countryStatsRepositoryMock;

    @Autowired
    private CountryStatsMapper countryStatsMapper;

    @Mock
    private CountryStatsService countryStatsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryStatsMockMvc;

    private CountryStats countryStats;

    private CountryStats insertedCountryStats;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountryStats createEntity() {
        return new CountryStats().year(DEFAULT_YEAR).population(DEFAULT_POPULATION).gdp(DEFAULT_GDP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountryStats createUpdatedEntity() {
        return new CountryStats().year(UPDATED_YEAR).population(UPDATED_POPULATION).gdp(UPDATED_GDP);
    }

    @BeforeEach
    void initTest() {
        countryStats = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCountryStats != null) {
            countryStatsRepository.delete(insertedCountryStats);
            insertedCountryStats = null;
        }
    }

    @Test
    @Transactional
    void createCountryStats() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);
        var returnedCountryStatsDTO = om.readValue(
            restCountryStatsMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryStatsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CountryStatsDTO.class
        );

        // Validate the CountryStats in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCountryStats = countryStatsMapper.toEntity(returnedCountryStatsDTO);
        assertCountryStatsUpdatableFieldsEquals(returnedCountryStats, getPersistedCountryStats(returnedCountryStats));

        insertedCountryStats = returnedCountryStats;
    }

    @Test
    @Transactional
    void createCountryStatsWithExistingId() throws Exception {
        // Create the CountryStats with an existing ID
        countryStats.setId(1L);
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryStatsMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        countryStats.setYear(null);

        // Create the CountryStats, which fails.
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        restCountryStatsMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountryStats() throws Exception {
        // Initialize the database
        insertedCountryStats = countryStatsRepository.saveAndFlush(countryStats);

        // Get all the countryStatsList
        restCountryStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countryStats.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION.intValue())))
            .andExpect(jsonPath("$.[*].gdp").value(hasItem(sameNumber(DEFAULT_GDP))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCountryStatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(countryStatsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCountryStatsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(countryStatsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCountryStatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(countryStatsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCountryStatsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(countryStatsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCountryStats() throws Exception {
        // Initialize the database
        insertedCountryStats = countryStatsRepository.saveAndFlush(countryStats);

        // Get the countryStats
        restCountryStatsMockMvc
            .perform(get(ENTITY_API_URL_ID, countryStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countryStats.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.population").value(DEFAULT_POPULATION.intValue()))
            .andExpect(jsonPath("$.gdp").value(sameNumber(DEFAULT_GDP)));
    }

    @Test
    @Transactional
    void getNonExistingCountryStats() throws Exception {
        // Get the countryStats
        restCountryStatsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountryStats() throws Exception {
        // Initialize the database
        insertedCountryStats = countryStatsRepository.saveAndFlush(countryStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the countryStats
        CountryStats updatedCountryStats = countryStatsRepository.findById(countryStats.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCountryStats are not directly saved in db
        em.detach(updatedCountryStats);
        updatedCountryStats.year(UPDATED_YEAR).population(UPDATED_POPULATION).gdp(UPDATED_GDP);
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(updatedCountryStats);

        restCountryStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryStatsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCountryStatsToMatchAllProperties(updatedCountryStats);
    }

    @Test
    @Transactional
    void putNonExistingCountryStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryStats.setId(longCount.incrementAndGet());

        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryStatsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountryStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryStats.setId(longCount.incrementAndGet());

        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountryStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryStats.setId(longCount.incrementAndGet());

        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryStatsMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryStatsWithPatch() throws Exception {
        // Initialize the database
        insertedCountryStats = countryStatsRepository.saveAndFlush(countryStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the countryStats using partial update
        CountryStats partialUpdatedCountryStats = new CountryStats();
        partialUpdatedCountryStats.setId(countryStats.getId());

        partialUpdatedCountryStats.year(UPDATED_YEAR).population(UPDATED_POPULATION).gdp(UPDATED_GDP);

        restCountryStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountryStats.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountryStats))
            )
            .andExpect(status().isOk());

        // Validate the CountryStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryStatsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCountryStats, countryStats),
            getPersistedCountryStats(countryStats)
        );
    }

    @Test
    @Transactional
    void fullUpdateCountryStatsWithPatch() throws Exception {
        // Initialize the database
        insertedCountryStats = countryStatsRepository.saveAndFlush(countryStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the countryStats using partial update
        CountryStats partialUpdatedCountryStats = new CountryStats();
        partialUpdatedCountryStats.setId(countryStats.getId());

        partialUpdatedCountryStats.year(UPDATED_YEAR).population(UPDATED_POPULATION).gdp(UPDATED_GDP);

        restCountryStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountryStats.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountryStats))
            )
            .andExpect(status().isOk());

        // Validate the CountryStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryStatsUpdatableFieldsEquals(partialUpdatedCountryStats, getPersistedCountryStats(partialUpdatedCountryStats));
    }

    @Test
    @Transactional
    void patchNonExistingCountryStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryStats.setId(longCount.incrementAndGet());

        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryStatsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountryStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryStats.setId(longCount.incrementAndGet());

        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountryStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryStats.setId(longCount.incrementAndGet());

        // Create the CountryStats
        CountryStatsDTO countryStatsDTO = countryStatsMapper.toDto(countryStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryStatsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryStatsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountryStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountryStats() throws Exception {
        // Initialize the database
        insertedCountryStats = countryStatsRepository.saveAndFlush(countryStats);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the countryStats
        restCountryStatsMockMvc
            .perform(delete(ENTITY_API_URL_ID, countryStats.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return countryStatsRepository.count();
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

    protected CountryStats getPersistedCountryStats(CountryStats countryStats) {
        return countryStatsRepository.findById(countryStats.getId()).orElseThrow();
    }

    protected void assertPersistedCountryStatsToMatchAllProperties(CountryStats expectedCountryStats) {
        assertCountryStatsAllPropertiesEquals(expectedCountryStats, getPersistedCountryStats(expectedCountryStats));
    }

    protected void assertPersistedCountryStatsToMatchUpdatableProperties(CountryStats expectedCountryStats) {
        assertCountryStatsAllUpdatablePropertiesEquals(expectedCountryStats, getPersistedCountryStats(expectedCountryStats));
    }
}
