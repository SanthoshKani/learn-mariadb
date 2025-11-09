package io.santhoshkani.learn.mariadb.web.rest;

import io.santhoshkani.learn.mariadb.repository.ContinentRepository;
import io.santhoshkani.learn.mariadb.service.ContinentService;
import io.santhoshkani.learn.mariadb.service.dto.ContinentDTO;
import io.santhoshkani.learn.mariadb.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.santhoshkani.learn.mariadb.domain.Continent}.
 */
@RestController
@RequestMapping("/api/continents")
public class ContinentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContinentResource.class);

    private static final String ENTITY_NAME = "continent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContinentService continentService;

    private final ContinentRepository continentRepository;

    public ContinentResource(ContinentService continentService, ContinentRepository continentRepository) {
        this.continentService = continentService;
        this.continentRepository = continentRepository;
    }

    /**
     * {@code POST  /continents} : Create a new continent.
     *
     * @param continentDTO the continentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new continentDTO, or with status {@code 400 (Bad Request)} if the continent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContinentDTO> createContinent(@Valid @RequestBody ContinentDTO continentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Continent : {}", continentDTO);
        if (continentDTO.getId() != null) {
            throw new BadRequestAlertException("A new continent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        continentDTO = continentService.save(continentDTO);
        return ResponseEntity.created(new URI("/api/continents/" + continentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, continentDTO.getId().toString()))
            .body(continentDTO);
    }

    /**
     * {@code PUT  /continents/:id} : Updates an existing continent.
     *
     * @param id the id of the continentDTO to save.
     * @param continentDTO the continentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated continentDTO,
     * or with status {@code 400 (Bad Request)} if the continentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the continentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContinentDTO> updateContinent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContinentDTO continentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Continent : {}, {}", id, continentDTO);
        if (continentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, continentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!continentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        continentDTO = continentService.update(continentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, continentDTO.getId().toString()))
            .body(continentDTO);
    }

    /**
     * {@code PATCH  /continents/:id} : Partial updates given fields of an existing continent, field will ignore if it is null
     *
     * @param id the id of the continentDTO to save.
     * @param continentDTO the continentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated continentDTO,
     * or with status {@code 400 (Bad Request)} if the continentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the continentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the continentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContinentDTO> partialUpdateContinent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContinentDTO continentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Continent partially : {}, {}", id, continentDTO);
        if (continentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, continentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!continentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContinentDTO> result = continentService.partialUpdate(continentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, continentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /continents} : get all the continents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of continents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContinentDTO>> getAllContinents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Continents");
        Page<ContinentDTO> page = continentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /continents/:id} : get the "id" continent.
     *
     * @param id the id of the continentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the continentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContinentDTO> getContinent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Continent : {}", id);
        Optional<ContinentDTO> continentDTO = continentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(continentDTO);
    }

    /**
     * {@code DELETE  /continents/:id} : delete the "id" continent.
     *
     * @param id the id of the continentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContinent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Continent : {}", id);
        continentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
