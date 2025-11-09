package io.santhoshkani.learn.mariadb.service.mapper;

import static io.santhoshkani.learn.mariadb.domain.GuestAsserts.*;
import static io.santhoshkani.learn.mariadb.domain.GuestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuestMapperTest {

    private GuestMapper guestMapper;

    @BeforeEach
    void setUp() {
        guestMapper = new GuestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGuestSample1();
        var actual = guestMapper.toEntity(guestMapper.toDto(expected));
        assertGuestAllPropertiesEquals(expected, actual);
    }
}
