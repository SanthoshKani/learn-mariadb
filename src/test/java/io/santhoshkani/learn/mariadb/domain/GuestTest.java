package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.EventTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.GuestTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.VipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GuestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guest.class);
        Guest guest1 = getGuestSample1();
        Guest guest2 = new Guest();
        assertThat(guest1).isNotEqualTo(guest2);

        guest2.setId(guest1.getId());
        assertThat(guest1).isEqualTo(guest2);

        guest2 = getGuestSample2();
        assertThat(guest1).isNotEqualTo(guest2);
    }

    @Test
    void vipTest() {
        Guest guest = getGuestRandomSampleGenerator();
        Vip vipBack = getVipRandomSampleGenerator();

        guest.addVip(vipBack);
        assertThat(guest.getVips()).containsOnly(vipBack);
        assertThat(vipBack.getGuests()).containsOnly(guest);

        guest.removeVip(vipBack);
        assertThat(guest.getVips()).doesNotContain(vipBack);
        assertThat(vipBack.getGuests()).doesNotContain(guest);

        guest.vips(new HashSet<>(Set.of(vipBack)));
        assertThat(guest.getVips()).containsOnly(vipBack);
        assertThat(vipBack.getGuests()).containsOnly(guest);

        guest.setVips(new HashSet<>());
        assertThat(guest.getVips()).doesNotContain(vipBack);
        assertThat(vipBack.getGuests()).doesNotContain(guest);
    }

    @Test
    void eventTest() {
        Guest guest = getGuestRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        guest.addEvent(eventBack);
        assertThat(guest.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getGuests()).containsOnly(guest);

        guest.removeEvent(eventBack);
        assertThat(guest.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getGuests()).doesNotContain(guest);

        guest.events(new HashSet<>(Set.of(eventBack)));
        assertThat(guest.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getGuests()).containsOnly(guest);

        guest.setEvents(new HashSet<>());
        assertThat(guest.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getGuests()).doesNotContain(guest);
    }
}
