package io.santhoshkani.learn.mariadb.web.rest;

import static io.santhoshkani.learn.mariadb.domain.LanguageAsserts.*;
import static io.santhoshkani.learn.mariadb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.santhoshkani.learn.mariadb.IntegrationTest;
import io.santhoshkani.learn.mariadb.domain.Language;
import io.santhoshkani.learn.mariadb.repository.LanguageRepository;
import io.santhoshkani.learn.mariadb.service.dto.LanguageDTO;
import io.santhoshkani.learn.mariadb.service.mapper.LanguageMapper;
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
 * Integration tests for the {@link LanguageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LanguageResourceIT {

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguageMockMvc;

    private Language language;

    private Language insertedLanguage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createEntity() {
        return new Language().language(DEFAULT_LANGUAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createUpdatedEntity() {
        return new Language().language(UPDATED_LANGUAGE);
    }

    @BeforeEach
    void initTest() {
        language = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLanguage != null) {
            languageRepository.delete(insertedLanguage);
            insertedLanguage = null;
        }
    }

    @Test
    @Transactional
    void createLanguage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);
        var returnedLanguageDTO = om.readValue(
            restLanguageMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LanguageDTO.class
        );

        // Validate the Language in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLanguage = languageMapper.toEntity(returnedLanguageDTO);
        assertLanguageUpdatableFieldsEquals(returnedLanguage, getPersistedLanguage(returnedLanguage));

        insertedLanguage = returnedLanguage;
    }

    @Test
    @Transactional
    void createLanguageWithExistingId() throws Exception {
        // Create the Language with an existing ID
        language.setId(1L);
        LanguageDTO languageDTO = languageMapper.toDto(language);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        language.setLanguage(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)));
    }

    @Test
    @Transactional
    void getLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE));
    }

    @Test
    @Transactional
    void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLanguage are not directly saved in db
        em.detach(updatedLanguage);
        updatedLanguage.language(UPDATED_LANGUAGE);
        LanguageDTO languageDTO = languageMapper.toDto(updatedLanguage);

        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLanguageToMatchAllProperties(updatedLanguage);
    }

    @Test
    @Transactional
    void putNonExistingLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.language(UPDATED_LANGUAGE);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLanguage, language), getPersistedLanguage(language));
    }

    @Test
    @Transactional
    void fullUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.language(UPDATED_LANGUAGE);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageUpdatableFieldsEquals(partialUpdatedLanguage, getPersistedLanguage(partialUpdatedLanguage));
    }

    @Test
    @Transactional
    void patchNonExistingLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, languageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanguage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        language.setId(longCount.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(languageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanguage() throws Exception {
        // Initialize the database
        insertedLanguage = languageRepository.saveAndFlush(language);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the language
        restLanguageMockMvc
            .perform(delete(ENTITY_API_URL_ID, language.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return languageRepository.count();
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

    protected Language getPersistedLanguage(Language language) {
        return languageRepository.findById(language.getId()).orElseThrow();
    }

    protected void assertPersistedLanguageToMatchAllProperties(Language expectedLanguage) {
        assertLanguageAllPropertiesEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
    }

    protected void assertPersistedLanguageToMatchUpdatableProperties(Language expectedLanguage) {
        assertLanguageAllUpdatablePropertiesEquals(expectedLanguage, getPersistedLanguage(expectedLanguage));
    }
}
