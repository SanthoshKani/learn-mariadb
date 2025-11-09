package io.santhoshkani.learn.mariadb.service.mapper;

import io.santhoshkani.learn.mariadb.domain.Continent;
import io.santhoshkani.learn.mariadb.service.dto.ContinentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Continent} and its DTO {@link ContinentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContinentMapper extends EntityMapper<ContinentDTO, Continent> {}
