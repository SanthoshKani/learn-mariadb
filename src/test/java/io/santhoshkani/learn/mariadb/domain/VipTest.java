package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.EventTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.GuestTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.VipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vip.class);
        Vip vip1 = getVipSample1();
        Vip vip2 = new Vip();
        assertThat(vip1).isNotEqualTo(vip2);

        vip2.setId(vip1.getId());
        assertThat(vip1).isEqualTo(vip2);

        vip2 = getVipSample2();
        assertThat(vip1).isNotEqualTo(vip2);
    }

    @Test
    void guestTest() {
        Vip vip = getVipRandomSampleGenerator();
        Guest guestBack = getGuestRandomSampleGenerator();

        vip.addGuest(guestBack);
        assertThat(vip.getGuests()).containsOnly(guestBack);

        vip.removeGuest(guestBack);
        assertThat(vip.getGuests()).doesNotContain(guestBack);

        vip.guests(new HashSet<>(Set.of(guestBack)));
        assertThat(vip.getGuests()).containsOnly(guestBack);

        vip.setGuests(new HashSet<>());
        assertThat(vip.getGuests()).doesNotContain(guestBack);
    }

    @Test
    void eventTest() {
        Vip vip = getVipRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        vip.addEvent(eventBack);
        assertThat(vip.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getVips()).containsOnly(vip);

        vip.removeEvent(eventBack);
        assertThat(vip.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getVips()).doesNotContain(vip);

        vip.events(new HashSet<>(Set.of(eventBack)));
        assertThat(vip.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getVips()).containsOnly(vip);

        vip.setEvents(new HashSet<>());
        assertThat(vip.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getVips()).doesNotContain(vip);
    }
}
