package io.santhoshkani.learn.mariadb.service.mapper;

import io.santhoshkani.learn.mariadb.domain.Country;
import io.santhoshkani.learn.mariadb.domain.CountryStats;
import io.santhoshkani.learn.mariadb.service.dto.CountryDTO;
import io.santhoshkani.learn.mariadb.service.dto.CountryStatsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountryStats} and its DTO {@link CountryStatsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryStatsMapper extends EntityMapper<CountryStatsDTO, CountryStats> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    CountryStatsDTO toDto(CountryStats s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
