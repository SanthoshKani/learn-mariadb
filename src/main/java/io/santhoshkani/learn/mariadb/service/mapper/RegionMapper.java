package io.santhoshkani.learn.mariadb.service.mapper;

import io.santhoshkani.learn.mariadb.domain.Continent;
import io.santhoshkani.learn.mariadb.domain.Region;
import io.santhoshkani.learn.mariadb.service.dto.ContinentDTO;
import io.santhoshkani.learn.mariadb.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {
    @Mapping(target = "continent", source = "continent", qualifiedByName = "continentName")
    RegionDTO toDto(Region s);

    @Named("continentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContinentDTO toDtoContinentName(Continent continent);
}
