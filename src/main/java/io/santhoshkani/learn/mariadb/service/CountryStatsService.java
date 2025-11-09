package io.santhoshkani.learn.mariadb.service;

import io.santhoshkani.learn.mariadb.domain.CountryStats;
import io.santhoshkani.learn.mariadb.repository.CountryStatsRepository;
import io.santhoshkani.learn.mariadb.service.dto.CountryStatsDTO;
import io.santhoshkani.learn.mariadb.service.mapper.CountryStatsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.santhoshkani.learn.mariadb.domain.CountryStats}.
 */
@Service
@Transactional
public class CountryStatsService {

    private static final Logger LOG = LoggerFactory.getLogger(CountryStatsService.class);

    private final CountryStatsRepository countryStatsRepository;

    private final CountryStatsMapper countryStatsMapper;

    public CountryStatsService(CountryStatsRepository countryStatsRepository, CountryStatsMapper countryStatsMapper) {
        this.countryStatsRepository = countryStatsRepository;
        this.countryStatsMapper = countryStatsMapper;
    }

    /**
     * Save a countryStats.
     *
     * @param countryStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryStatsDTO save(CountryStatsDTO countryStatsDTO) {
        LOG.debug("Request to save CountryStats : {}", countryStatsDTO);
        CountryStats countryStats = countryStatsMapper.toEntity(countryStatsDTO);
        countryStats = countryStatsRepository.save(countryStats);
        return countryStatsMapper.toDto(countryStats);
    }

    /**
     * Update a countryStats.
     *
     * @param countryStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryStatsDTO update(CountryStatsDTO countryStatsDTO) {
        LOG.debug("Request to update CountryStats : {}", countryStatsDTO);
        CountryStats countryStats = countryStatsMapper.toEntity(countryStatsDTO);
        countryStats = countryStatsRepository.save(countryStats);
        return countryStatsMapper.toDto(countryStats);
    }

    /**
     * Partially update a countryStats.
     *
     * @param countryStatsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountryStatsDTO> partialUpdate(CountryStatsDTO countryStatsDTO) {
        LOG.debug("Request to partially update CountryStats : {}", countryStatsDTO);

        return countryStatsRepository
            .findById(countryStatsDTO.getId())
            .map(existingCountryStats -> {
                countryStatsMapper.partialUpdate(existingCountryStats, countryStatsDTO);

                return existingCountryStats;
            })
            .map(countryStatsRepository::save)
            .map(countryStatsMapper::toDto);
    }

    /**
     * Get all the countryStats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryStatsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CountryStats");
        return countryStatsRepository.findAll(pageable).map(countryStatsMapper::toDto);
    }

    /**
     * Get all the countryStats with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CountryStatsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return countryStatsRepository.findAllWithEagerRelationships(pageable).map(countryStatsMapper::toDto);
    }

    /**
     * Get one countryStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountryStatsDTO> findOne(Long id) {
        LOG.debug("Request to get CountryStats : {}", id);
        return countryStatsRepository.findOneWithEagerRelationships(id).map(countryStatsMapper::toDto);
    }

    /**
     * Delete the countryStats by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CountryStats : {}", id);
        countryStatsRepository.deleteById(id);
    }
}
