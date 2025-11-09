package io.santhoshkani.learn.mariadb.service.mapper;

import static io.santhoshkani.learn.mariadb.domain.EventAsserts.*;
import static io.santhoshkani.learn.mariadb.domain.EventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventMapperTest {

    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        eventMapper = new EventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventSample1();
        var actual = eventMapper.toEntity(eventMapper.toDto(expected));
        assertEventAllPropertiesEquals(expected, actual);
    }
}
