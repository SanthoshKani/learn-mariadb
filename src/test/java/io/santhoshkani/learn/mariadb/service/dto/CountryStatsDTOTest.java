package io.santhoshkani.learn.mariadb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryStatsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryStatsDTO.class);
        CountryStatsDTO countryStatsDTO1 = new CountryStatsDTO();
        countryStatsDTO1.setId(1L);
        CountryStatsDTO countryStatsDTO2 = new CountryStatsDTO();
        assertThat(countryStatsDTO1).isNotEqualTo(countryStatsDTO2);
        countryStatsDTO2.setId(countryStatsDTO1.getId());
        assertThat(countryStatsDTO1).isEqualTo(countryStatsDTO2);
        countryStatsDTO2.setId(2L);
        assertThat(countryStatsDTO1).isNotEqualTo(countryStatsDTO2);
        countryStatsDTO1.setId(null);
        assertThat(countryStatsDTO1).isNotEqualTo(countryStatsDTO2);
    }
}
