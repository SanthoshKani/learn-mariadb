package io.santhoshkani.learn.mariadb.service;

import io.santhoshkani.learn.mariadb.domain.Vip;
import io.santhoshkani.learn.mariadb.repository.VipRepository;
import io.santhoshkani.learn.mariadb.service.dto.VipDTO;
import io.santhoshkani.learn.mariadb.service.mapper.VipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.santhoshkani.learn.mariadb.domain.Vip}.
 */
@Service
@Transactional
public class VipService {

    private static final Logger LOG = LoggerFactory.getLogger(VipService.class);

    private final VipRepository vipRepository;

    private final VipMapper vipMapper;

    public VipService(VipRepository vipRepository, VipMapper vipMapper) {
        this.vipRepository = vipRepository;
        this.vipMapper = vipMapper;
    }

    /**
     * Save a vip.
     *
     * @param vipDTO the entity to save.
     * @return the persisted entity.
     */
    public VipDTO save(VipDTO vipDTO) {
        LOG.debug("Request to save Vip : {}", vipDTO);
        Vip vip = vipMapper.toEntity(vipDTO);
        vip = vipRepository.save(vip);
        return vipMapper.toDto(vip);
    }

    /**
     * Update a vip.
     *
     * @param vipDTO the entity to save.
     * @return the persisted entity.
     */
    public VipDTO update(VipDTO vipDTO) {
        LOG.debug("Request to update Vip : {}", vipDTO);
        Vip vip = vipMapper.toEntity(vipDTO);
        vip = vipRepository.save(vip);
        return vipMapper.toDto(vip);
    }

    /**
     * Partially update a vip.
     *
     * @param vipDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VipDTO> partialUpdate(VipDTO vipDTO) {
        LOG.debug("Request to partially update Vip : {}", vipDTO);

        return vipRepository
            .findById(vipDTO.getId())
            .map(existingVip -> {
                vipMapper.partialUpdate(existingVip, vipDTO);

                return existingVip;
            })
            .map(vipRepository::save)
            .map(vipMapper::toDto);
    }

    /**
     * Get all the vips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VipDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vips");
        return vipRepository.findAll(pageable).map(vipMapper::toDto);
    }

    /**
     * Get all the vips with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VipDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vipRepository.findAllWithEagerRelationships(pageable).map(vipMapper::toDto);
    }

    /**
     * Get one vip by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VipDTO> findOne(Long id) {
        LOG.debug("Request to get Vip : {}", id);
        return vipRepository.findOneWithEagerRelationships(id).map(vipMapper::toDto);
    }

    /**
     * Delete the vip by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vip : {}", id);
        vipRepository.deleteById(id);
    }
}
