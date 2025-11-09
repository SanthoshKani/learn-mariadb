package io.santhoshkani.learn.mariadb.service.mapper;

import static io.santhoshkani.learn.mariadb.domain.ContinentAsserts.*;
import static io.santhoshkani.learn.mariadb.domain.ContinentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContinentMapperTest {

    private ContinentMapper continentMapper;

    @BeforeEach
    void setUp() {
        continentMapper = new ContinentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContinentSample1();
        var actual = continentMapper.toEntity(continentMapper.toDto(expected));
        assertContinentAllPropertiesEquals(expected, actual);
    }
}
