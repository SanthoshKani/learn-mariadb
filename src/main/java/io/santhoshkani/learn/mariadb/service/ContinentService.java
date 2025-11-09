package io.santhoshkani.learn.mariadb.service;

import io.santhoshkani.learn.mariadb.domain.Continent;
import io.santhoshkani.learn.mariadb.repository.ContinentRepository;
import io.santhoshkani.learn.mariadb.service.dto.ContinentDTO;
import io.santhoshkani.learn.mariadb.service.mapper.ContinentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.santhoshkani.learn.mariadb.domain.Continent}.
 */
@Service
@Transactional
public class ContinentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContinentService.class);

    private final ContinentRepository continentRepository;

    private final ContinentMapper continentMapper;

    public ContinentService(ContinentRepository continentRepository, ContinentMapper continentMapper) {
        this.continentRepository = continentRepository;
        this.continentMapper = continentMapper;
    }

    /**
     * Save a continent.
     *
     * @param continentDTO the entity to save.
     * @return the persisted entity.
     */
    public ContinentDTO save(ContinentDTO continentDTO) {
        LOG.debug("Request to save Continent : {}", continentDTO);
        Continent continent = continentMapper.toEntity(continentDTO);
        continent = continentRepository.save(continent);
        return continentMapper.toDto(continent);
    }

    /**
     * Update a continent.
     *
     * @param continentDTO the entity to save.
     * @return the persisted entity.
     */
    public ContinentDTO update(ContinentDTO continentDTO) {
        LOG.debug("Request to update Continent : {}", continentDTO);
        Continent continent = continentMapper.toEntity(continentDTO);
        continent = continentRepository.save(continent);
        return continentMapper.toDto(continent);
    }

    /**
     * Partially update a continent.
     *
     * @param continentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContinentDTO> partialUpdate(ContinentDTO continentDTO) {
        LOG.debug("Request to partially update Continent : {}", continentDTO);

        return continentRepository
            .findById(continentDTO.getId())
            .map(existingContinent -> {
                continentMapper.partialUpdate(existingContinent, continentDTO);

                return existingContinent;
            })
            .map(continentRepository::save)
            .map(continentMapper::toDto);
    }

    /**
     * Get all the continents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContinentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Continents");
        return continentRepository.findAll(pageable).map(continentMapper::toDto);
    }

    /**
     * Get one continent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContinentDTO> findOne(Long id) {
        LOG.debug("Request to get Continent : {}", id);
        return continentRepository.findById(id).map(continentMapper::toDto);
    }

    /**
     * Delete the continent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Continent : {}", id);
        continentRepository.deleteById(id);
    }
}
