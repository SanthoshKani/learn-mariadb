package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.CountryStatsTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.CountryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryStatsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryStats.class);
        CountryStats countryStats1 = getCountryStatsSample1();
        CountryStats countryStats2 = new CountryStats();
        assertThat(countryStats1).isNotEqualTo(countryStats2);

        countryStats2.setId(countryStats1.getId());
        assertThat(countryStats1).isEqualTo(countryStats2);

        countryStats2 = getCountryStatsSample2();
        assertThat(countryStats1).isNotEqualTo(countryStats2);
    }

    @Test
    void countryTest() {
        CountryStats countryStats = getCountryStatsRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        countryStats.setCountry(countryBack);
        assertThat(countryStats.getCountry()).isEqualTo(countryBack);

        countryStats.country(null);
        assertThat(countryStats.getCountry()).isNull();
    }
}
