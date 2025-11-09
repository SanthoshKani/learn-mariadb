package io.santhoshkani.learn.mariadb.web.rest;

import static io.santhoshkani.learn.mariadb.domain.CountryAsserts.*;
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
import io.santhoshkani.learn.mariadb.domain.Country;
import io.santhoshkani.learn.mariadb.repository.CountryRepository;
import io.santhoshkani.learn.mariadb.service.CountryService;
import io.santhoshkani.learn.mariadb.service.dto.CountryDTO;
import io.santhoshkani.learn.mariadb.service.mapper.CountryMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AREA = new BigDecimal(2);

    private static final LocalDate DEFAULT_NATIONAL_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NATIONAL_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COUNTRY_CODE_2 = "AA";
    private static final String UPDATED_COUNTRY_CODE_2 = "BB";

    private static final String DEFAULT_COUNTRY_CODE_3 = "AAA";
    private static final String UPDATED_COUNTRY_CODE_3 = "BBB";

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CountryRepository countryRepository;

    @Mock
    private CountryRepository countryRepositoryMock;

    @Autowired
    private CountryMapper countryMapper;

    @Mock
    private CountryService countryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    private Country insertedCountry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity() {
        return new Country()
            .name(DEFAULT_NAME)
            .area(DEFAULT_AREA)
            .nationalDay(DEFAULT_NATIONAL_DAY)
            .countryCode2(DEFAULT_COUNTRY_CODE_2)
            .countryCode3(DEFAULT_COUNTRY_CODE_3);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity() {
        return new Country()
            .name(UPDATED_NAME)
            .area(UPDATED_AREA)
            .nationalDay(UPDATED_NATIONAL_DAY)
            .countryCode2(UPDATED_COUNTRY_CODE_2)
            .countryCode3(UPDATED_COUNTRY_CODE_3);
    }

    @BeforeEach
    void initTest() {
        country = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCountry != null) {
            countryRepository.delete(insertedCountry);
            insertedCountry = null;
        }
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        var returnedCountryDTO = om.readValue(
            restCountryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CountryDTO.class
        );

        // Validate the Country in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCountry = countryMapper.toEntity(returnedCountryDTO);
        assertCountryUpdatableFieldsEquals(returnedCountry, getPersistedCountry(returnedCountry));

        insertedCountry = returnedCountry;
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        country.setName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAreaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        country.setArea(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryCode2IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        country.setCountryCode2(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryCode3IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        country.setCountryCode3(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(sameNumber(DEFAULT_AREA))))
            .andExpect(jsonPath("$.[*].nationalDay").value(hasItem(DEFAULT_NATIONAL_DAY.toString())))
            .andExpect(jsonPath("$.[*].countryCode2").value(hasItem(DEFAULT_COUNTRY_CODE_2)))
            .andExpect(jsonPath("$.[*].countryCode3").value(hasItem(DEFAULT_COUNTRY_CODE_3)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCountriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(countryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCountryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(countryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCountriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(countryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCountryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(countryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.area").value(sameNumber(DEFAULT_AREA)))
            .andExpect(jsonPath("$.nationalDay").value(DEFAULT_NATIONAL_DAY.toString()))
            .andExpect(jsonPath("$.countryCode2").value(DEFAULT_COUNTRY_CODE_2))
            .andExpect(jsonPath("$.countryCode3").value(DEFAULT_COUNTRY_CODE_3));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .name(UPDATED_NAME)
            .area(UPDATED_AREA)
            .nationalDay(UPDATED_NATIONAL_DAY)
            .countryCode2(UPDATED_COUNTRY_CODE_2)
            .countryCode3(UPDATED_COUNTRY_CODE_3);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCountryToMatchAllProperties(updatedCountry);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .area(UPDATED_AREA)
            .nationalDay(UPDATED_NATIONAL_DAY)
            .countryCode2(UPDATED_COUNTRY_CODE_2)
            .countryCode3(UPDATED_COUNTRY_CODE_3);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCountry, country), getPersistedCountry(country));
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .name(UPDATED_NAME)
            .area(UPDATED_AREA)
            .nationalDay(UPDATED_NATIONAL_DAY)
            .countryCode2(UPDATED_COUNTRY_CODE_2)
            .countryCode3(UPDATED_COUNTRY_CODE_3);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(partialUpdatedCountry, getPersistedCountry(partialUpdatedCountry));
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return countryRepository.count();
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

    protected Country getPersistedCountry(Country country) {
        return countryRepository.findById(country.getId()).orElseThrow();
    }

    protected void assertPersistedCountryToMatchAllProperties(Country expectedCountry) {
        assertCountryAllPropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }

    protected void assertPersistedCountryToMatchUpdatableProperties(Country expectedCountry) {
        assertCountryAllUpdatablePropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }
}
