package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.CountryStatsTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.CountryTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.LanguageTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void countryStatTest() {
        Country country = getCountryRandomSampleGenerator();
        CountryStats countryStatsBack = getCountryStatsRandomSampleGenerator();

        country.addCountryStat(countryStatsBack);
        assertThat(country.getCountryStats()).containsOnly(countryStatsBack);
        assertThat(countryStatsBack.getCountry()).isEqualTo(country);

        country.removeCountryStat(countryStatsBack);
        assertThat(country.getCountryStats()).doesNotContain(countryStatsBack);
        assertThat(countryStatsBack.getCountry()).isNull();

        country.countryStats(new HashSet<>(Set.of(countryStatsBack)));
        assertThat(country.getCountryStats()).containsOnly(countryStatsBack);
        assertThat(countryStatsBack.getCountry()).isEqualTo(country);

        country.setCountryStats(new HashSet<>());
        assertThat(country.getCountryStats()).doesNotContain(countryStatsBack);
        assertThat(countryStatsBack.getCountry()).isNull();
    }

    @Test
    void languageTest() {
        Country country = getCountryRandomSampleGenerator();
        Language languageBack = getLanguageRandomSampleGenerator();

        country.addLanguage(languageBack);
        assertThat(country.getLanguages()).containsOnly(languageBack);

        country.removeLanguage(languageBack);
        assertThat(country.getLanguages()).doesNotContain(languageBack);

        country.languages(new HashSet<>(Set.of(languageBack)));
        assertThat(country.getLanguages()).containsOnly(languageBack);

        country.setLanguages(new HashSet<>());
        assertThat(country.getLanguages()).doesNotContain(languageBack);
    }

    @Test
    void regionTest() {
        Country country = getCountryRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        country.setRegion(regionBack);
        assertThat(country.getRegion()).isEqualTo(regionBack);

        country.region(null);
        assertThat(country.getRegion()).isNull();
    }
}
