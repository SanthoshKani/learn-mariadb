package io.santhoshkani.learn.mariadb.service.mapper;

import io.santhoshkani.learn.mariadb.domain.Country;
import io.santhoshkani.learn.mariadb.domain.Language;
import io.santhoshkani.learn.mariadb.domain.Region;
import io.santhoshkani.learn.mariadb.service.dto.CountryDTO;
import io.santhoshkani.learn.mariadb.service.dto.LanguageDTO;
import io.santhoshkani.learn.mariadb.service.dto.RegionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {
    @Mapping(target = "languages", source = "languages", qualifiedByName = "languageLanguageSet")
    @Mapping(target = "region", source = "region", qualifiedByName = "regionName")
    CountryDTO toDto(Country s);

    @Mapping(target = "removeLanguage", ignore = true)
    Country toEntity(CountryDTO countryDTO);

    @Named("languageLanguage")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "language", source = "language")
    LanguageDTO toDtoLanguageLanguage(Language language);

    @Named("languageLanguageSet")
    default Set<LanguageDTO> toDtoLanguageLanguageSet(Set<Language> language) {
        return language.stream().map(this::toDtoLanguageLanguage).collect(Collectors.toSet());
    }

    @Named("regionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionDTO toDtoRegionName(Region region);
}
