package io.santhoshkani.learn.mariadb.service.mapper;

import static io.santhoshkani.learn.mariadb.domain.CountryStatsAsserts.*;
import static io.santhoshkani.learn.mariadb.domain.CountryStatsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryStatsMapperTest {

    private CountryStatsMapper countryStatsMapper;

    @BeforeEach
    void setUp() {
        countryStatsMapper = new CountryStatsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCountryStatsSample1();
        var actual = countryStatsMapper.toEntity(countryStatsMapper.toDto(expected));
        assertCountryStatsAllPropertiesEquals(expected, actual);
    }
}
