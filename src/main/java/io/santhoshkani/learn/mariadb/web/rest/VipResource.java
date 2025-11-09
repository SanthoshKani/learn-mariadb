package io.santhoshkani.learn.mariadb.web.rest;

import io.santhoshkani.learn.mariadb.repository.VipRepository;
import io.santhoshkani.learn.mariadb.service.VipService;
import io.santhoshkani.learn.mariadb.service.dto.VipDTO;
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
 * REST controller for managing {@link io.santhoshkani.learn.mariadb.domain.Vip}.
 */
@RestController
@RequestMapping("/api/vips")
public class VipResource {

    private static final Logger LOG = LoggerFactory.getLogger(VipResource.class);

    private static final String ENTITY_NAME = "vip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VipService vipService;

    private final VipRepository vipRepository;

    public VipResource(VipService vipService, VipRepository vipRepository) {
        this.vipService = vipService;
        this.vipRepository = vipRepository;
    }

    /**
     * {@code POST  /vips} : Create a new vip.
     *
     * @param vipDTO the vipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vipDTO, or with status {@code 400 (Bad Request)} if the vip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VipDTO> createVip(@Valid @RequestBody VipDTO vipDTO) throws URISyntaxException {
        LOG.debug("REST request to save Vip : {}", vipDTO);
        if (vipDTO.getId() != null) {
            throw new BadRequestAlertException("A new vip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vipDTO = vipService.save(vipDTO);
        return ResponseEntity.created(new URI("/api/vips/" + vipDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vipDTO.getId().toString()))
            .body(vipDTO);
    }

    /**
     * {@code PUT  /vips/:id} : Updates an existing vip.
     *
     * @param id the id of the vipDTO to save.
     * @param vipDTO the vipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vipDTO,
     * or with status {@code 400 (Bad Request)} if the vipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VipDTO> updateVip(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody VipDTO vipDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Vip : {}, {}", id, vipDTO);
        if (vipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vipDTO = vipService.update(vipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vipDTO.getId().toString()))
            .body(vipDTO);
    }

    /**
     * {@code PATCH  /vips/:id} : Partial updates given fields of an existing vip, field will ignore if it is null
     *
     * @param id the id of the vipDTO to save.
     * @param vipDTO the vipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vipDTO,
     * or with status {@code 400 (Bad Request)} if the vipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VipDTO> partialUpdateVip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VipDTO vipDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vip partially : {}, {}", id, vipDTO);
        if (vipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VipDTO> result = vipService.partialUpdate(vipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vipDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vips} : get all the vips.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vips in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VipDTO>> getAllVips(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Vips");
        Page<VipDTO> page;
        if (eagerload) {
            page = vipService.findAllWithEagerRelationships(pageable);
        } else {
            page = vipService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vips/:id} : get the "id" vip.
     *
     * @param id the id of the vipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VipDTO> getVip(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vip : {}", id);
        Optional<VipDTO> vipDTO = vipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vipDTO);
    }

    /**
     * {@code DELETE  /vips/:id} : delete the "id" vip.
     *
     * @param id the id of the vipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVip(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vip : {}", id);
        vipService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
