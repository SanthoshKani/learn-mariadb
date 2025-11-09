package io.santhoshkani.learn.mariadb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuestDTO.class);
        GuestDTO guestDTO1 = new GuestDTO();
        guestDTO1.setId(1L);
        GuestDTO guestDTO2 = new GuestDTO();
        assertThat(guestDTO1).isNotEqualTo(guestDTO2);
        guestDTO2.setId(guestDTO1.getId());
        assertThat(guestDTO1).isEqualTo(guestDTO2);
        guestDTO2.setId(2L);
        assertThat(guestDTO1).isNotEqualTo(guestDTO2);
        guestDTO1.setId(null);
        assertThat(guestDTO1).isNotEqualTo(guestDTO2);
    }
}
