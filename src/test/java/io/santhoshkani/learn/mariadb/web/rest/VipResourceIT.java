package io.santhoshkani.learn.mariadb.web.rest;

import static io.santhoshkani.learn.mariadb.domain.VipAsserts.*;
import static io.santhoshkani.learn.mariadb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.santhoshkani.learn.mariadb.IntegrationTest;
import io.santhoshkani.learn.mariadb.domain.Vip;
import io.santhoshkani.learn.mariadb.repository.VipRepository;
import io.santhoshkani.learn.mariadb.service.VipService;
import io.santhoshkani.learn.mariadb.service.dto.VipDTO;
import io.santhoshkani.learn.mariadb.service.mapper.VipMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link VipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VipResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VipRepository vipRepository;

    @Mock
    private VipRepository vipRepositoryMock;

    @Autowired
    private VipMapper vipMapper;

    @Mock
    private VipService vipServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVipMockMvc;

    private Vip vip;

    private Vip insertedVip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vip createEntity() {
        return new Vip().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vip createUpdatedEntity() {
        return new Vip().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        vip = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVip != null) {
            vipRepository.delete(insertedVip);
            insertedVip = null;
        }
    }

    @Test
    @Transactional
    void createVip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);
        var returnedVipDTO = om.readValue(
            restVipMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vipDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VipDTO.class
        );

        // Validate the Vip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVip = vipMapper.toEntity(returnedVipDTO);
        assertVipUpdatableFieldsEquals(returnedVip, getPersistedVip(returnedVip));

        insertedVip = returnedVip;
    }

    @Test
    @Transactional
    void createVipWithExistingId() throws Exception {
        // Create the Vip with an existing ID
        vip.setId(1L);
        VipDTO vipDTO = vipMapper.toDto(vip);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVipMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vip.setName(null);

        // Create the Vip, which fails.
        VipDTO vipDTO = vipMapper.toDto(vip);

        restVipMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vipDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVips() throws Exception {
        // Initialize the database
        insertedVip = vipRepository.saveAndFlush(vip);

        // Get all the vipList
        restVipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vip.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVip() throws Exception {
        // Initialize the database
        insertedVip = vipRepository.saveAndFlush(vip);

        // Get the vip
        restVipMockMvc
            .perform(get(ENTITY_API_URL_ID, vip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vip.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVip() throws Exception {
        // Get the vip
        restVipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVip() throws Exception {
        // Initialize the database
        insertedVip = vipRepository.saveAndFlush(vip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vip
        Vip updatedVip = vipRepository.findById(vip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVip are not directly saved in db
        em.detach(updatedVip);
        updatedVip.name(UPDATED_NAME);
        VipDTO vipDTO = vipMapper.toDto(updatedVip);

        restVipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vipDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vipDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVipToMatchAllProperties(updatedVip);
    }

    @Test
    @Transactional
    void putNonExistingVip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vip.setId(longCount.incrementAndGet());

        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vipDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vip.setId(longCount.incrementAndGet());

        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vip.setId(longCount.incrementAndGet());

        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVipMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVipWithPatch() throws Exception {
        // Initialize the database
        insertedVip = vipRepository.saveAndFlush(vip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vip using partial update
        Vip partialUpdatedVip = new Vip();
        partialUpdatedVip.setId(vip.getId());

        restVipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVip.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVip))
            )
            .andExpect(status().isOk());

        // Validate the Vip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVipUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVip, vip), getPersistedVip(vip));
    }

    @Test
    @Transactional
    void fullUpdateVipWithPatch() throws Exception {
        // Initialize the database
        insertedVip = vipRepository.saveAndFlush(vip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vip using partial update
        Vip partialUpdatedVip = new Vip();
        partialUpdatedVip.setId(vip.getId());

        partialUpdatedVip.name(UPDATED_NAME);

        restVipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVip.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVip))
            )
            .andExpect(status().isOk());

        // Validate the Vip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVipUpdatableFieldsEquals(partialUpdatedVip, getPersistedVip(partialUpdatedVip));
    }

    @Test
    @Transactional
    void patchNonExistingVip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vip.setId(longCount.incrementAndGet());

        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vipDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vip.setId(longCount.incrementAndGet());

        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vip.setId(longCount.incrementAndGet());

        // Create the Vip
        VipDTO vipDTO = vipMapper.toDto(vip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVipMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVip() throws Exception {
        // Initialize the database
        insertedVip = vipRepository.saveAndFlush(vip);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vip
        restVipMockMvc
            .perform(delete(ENTITY_API_URL_ID, vip.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vipRepository.count();
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

    protected Vip getPersistedVip(Vip vip) {
        return vipRepository.findById(vip.getId()).orElseThrow();
    }

    protected void assertPersistedVipToMatchAllProperties(Vip expectedVip) {
        assertVipAllPropertiesEquals(expectedVip, getPersistedVip(expectedVip));
    }

    protected void assertPersistedVipToMatchUpdatableProperties(Vip expectedVip) {
        assertVipAllUpdatablePropertiesEquals(expectedVip, getPersistedVip(expectedVip));
    }
}
