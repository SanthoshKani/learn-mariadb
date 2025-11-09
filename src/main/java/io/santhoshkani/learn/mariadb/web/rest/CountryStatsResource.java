package io.santhoshkani.learn.mariadb.web.rest;

import io.santhoshkani.learn.mariadb.repository.CountryStatsRepository;
import io.santhoshkani.learn.mariadb.service.CountryStatsService;
import io.santhoshkani.learn.mariadb.service.dto.CountryStatsDTO;
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
 * REST controller for managing {@link io.santhoshkani.learn.mariadb.domain.CountryStats}.
 */
@RestController
@RequestMapping("/api/country-stats")
public class CountryStatsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CountryStatsResource.class);

    private static final String ENTITY_NAME = "countryStats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountryStatsService countryStatsService;

    private final CountryStatsRepository countryStatsRepository;

    public CountryStatsResource(CountryStatsService countryStatsService, CountryStatsRepository countryStatsRepository) {
        this.countryStatsService = countryStatsService;
        this.countryStatsRepository = countryStatsRepository;
    }

    /**
     * {@code POST  /country-stats} : Create a new countryStats.
     *
     * @param countryStatsDTO the countryStatsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countryStatsDTO, or with status {@code 400 (Bad Request)} if the countryStats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CountryStatsDTO> createCountryStats(@Valid @RequestBody CountryStatsDTO countryStatsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CountryStats : {}", countryStatsDTO);
        if (countryStatsDTO.getId() != null) {
            throw new BadRequestAlertException("A new countryStats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        countryStatsDTO = countryStatsService.save(countryStatsDTO);
        return ResponseEntity.created(new URI("/api/country-stats/" + countryStatsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, countryStatsDTO.getId().toString()))
            .body(countryStatsDTO);
    }

    /**
     * {@code PUT  /country-stats/:id} : Updates an existing countryStats.
     *
     * @param id the id of the countryStatsDTO to save.
     * @param countryStatsDTO the countryStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryStatsDTO,
     * or with status {@code 400 (Bad Request)} if the countryStatsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countryStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CountryStatsDTO> updateCountryStats(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountryStatsDTO countryStatsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CountryStats : {}, {}", id, countryStatsDTO);
        if (countryStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryStatsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        countryStatsDTO = countryStatsService.update(countryStatsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countryStatsDTO.getId().toString()))
            .body(countryStatsDTO);
    }

    /**
     * {@code PATCH  /country-stats/:id} : Partial updates given fields of an existing countryStats, field will ignore if it is null
     *
     * @param id the id of the countryStatsDTO to save.
     * @param countryStatsDTO the countryStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryStatsDTO,
     * or with status {@code 400 (Bad Request)} if the countryStatsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the countryStatsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the countryStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountryStatsDTO> partialUpdateCountryStats(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountryStatsDTO countryStatsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CountryStats partially : {}, {}", id, countryStatsDTO);
        if (countryStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryStatsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountryStatsDTO> result = countryStatsService.partialUpdate(countryStatsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countryStatsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /country-stats} : get all the countryStats.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countryStats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CountryStatsDTO>> getAllCountryStats(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CountryStats");
        Page<CountryStatsDTO> page;
        if (eagerload) {
            page = countryStatsService.findAllWithEagerRelationships(pageable);
        } else {
            page = countryStatsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /country-stats/:id} : get the "id" countryStats.
     *
     * @param id the id of the countryStatsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countryStatsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryStatsDTO> getCountryStats(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CountryStats : {}", id);
        Optional<CountryStatsDTO> countryStatsDTO = countryStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countryStatsDTO);
    }

    /**
     * {@code DELETE  /country-stats/:id} : delete the "id" countryStats.
     *
     * @param id the id of the countryStatsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountryStats(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CountryStats : {}", id);
        countryStatsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
