package io.santhoshkani.learn.mariadb.service.mapper;

import io.santhoshkani.learn.mariadb.domain.Country;
import io.santhoshkani.learn.mariadb.domain.Language;
import io.santhoshkani.learn.mariadb.service.dto.CountryDTO;
import io.santhoshkani.learn.mariadb.service.dto.LanguageDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Language} and its DTO {@link LanguageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {
    @Mapping(target = "countries", source = "countries", qualifiedByName = "countryNameSet")
    LanguageDTO toDto(Language s);

    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "removeCountry", ignore = true)
    Language toEntity(LanguageDTO languageDTO);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);

    @Named("countryNameSet")
    default Set<CountryDTO> toDtoCountryNameSet(Set<Country> country) {
        return country.stream().map(this::toDtoCountryName).collect(Collectors.toSet());
    }
}
