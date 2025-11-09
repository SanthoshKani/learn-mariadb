package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.EventTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.GuestTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.VipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = getEventSample1();
        Event event2 = new Event();
        assertThat(event1).isNotEqualTo(event2);

        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);

        event2 = getEventSample2();
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void vipTest() {
        Event event = getEventRandomSampleGenerator();
        Vip vipBack = getVipRandomSampleGenerator();

        event.addVip(vipBack);
        assertThat(event.getVips()).containsOnly(vipBack);

        event.removeVip(vipBack);
        assertThat(event.getVips()).doesNotContain(vipBack);

        event.vips(new HashSet<>(Set.of(vipBack)));
        assertThat(event.getVips()).containsOnly(vipBack);

        event.setVips(new HashSet<>());
        assertThat(event.getVips()).doesNotContain(vipBack);
    }

    @Test
    void guestTest() {
        Event event = getEventRandomSampleGenerator();
        Guest guestBack = getGuestRandomSampleGenerator();

        event.addGuest(guestBack);
        assertThat(event.getGuests()).containsOnly(guestBack);

        event.removeGuest(guestBack);
        assertThat(event.getGuests()).doesNotContain(guestBack);

        event.guests(new HashSet<>(Set.of(guestBack)));
        assertThat(event.getGuests()).containsOnly(guestBack);

        event.setGuests(new HashSet<>());
        assertThat(event.getGuests()).doesNotContain(guestBack);
    }
}
