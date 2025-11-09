package io.santhoshkani.learn.mariadb.service.mapper;

import static io.santhoshkani.learn.mariadb.domain.VipAsserts.*;
import static io.santhoshkani.learn.mariadb.domain.VipTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VipMapperTest {

    private VipMapper vipMapper;

    @BeforeEach
    void setUp() {
        vipMapper = new VipMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVipSample1();
        var actual = vipMapper.toEntity(vipMapper.toDto(expected));
        assertVipAllPropertiesEquals(expected, actual);
    }
}
