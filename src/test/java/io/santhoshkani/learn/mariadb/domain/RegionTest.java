package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.ContinentTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.CountryTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Region.class);
        Region region1 = getRegionSample1();
        Region region2 = new Region();
        assertThat(region1).isNotEqualTo(region2);

        region2.setId(region1.getId());
        assertThat(region1).isEqualTo(region2);

        region2 = getRegionSample2();
        assertThat(region1).isNotEqualTo(region2);
    }

    @Test
    void countryTest() {
        Region region = getRegionRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        region.addCountry(countryBack);
        assertThat(region.getCountries()).containsOnly(countryBack);
        assertThat(countryBack.getRegion()).isEqualTo(region);

        region.removeCountry(countryBack);
        assertThat(region.getCountries()).doesNotContain(countryBack);
        assertThat(countryBack.getRegion()).isNull();

        region.countries(new HashSet<>(Set.of(countryBack)));
        assertThat(region.getCountries()).containsOnly(countryBack);
        assertThat(countryBack.getRegion()).isEqualTo(region);

        region.setCountries(new HashSet<>());
        assertThat(region.getCountries()).doesNotContain(countryBack);
        assertThat(countryBack.getRegion()).isNull();
    }

    @Test
    void continentTest() {
        Region region = getRegionRandomSampleGenerator();
        Continent continentBack = getContinentRandomSampleGenerator();

        region.setContinent(continentBack);
        assertThat(region.getContinent()).isEqualTo(continentBack);

        region.continent(null);
        assertThat(region.getContinent()).isNull();
    }
}
