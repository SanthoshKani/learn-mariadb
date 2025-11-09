package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.ContinentTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContinentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Continent.class);
        Continent continent1 = getContinentSample1();
        Continent continent2 = new Continent();
        assertThat(continent1).isNotEqualTo(continent2);

        continent2.setId(continent1.getId());
        assertThat(continent1).isEqualTo(continent2);

        continent2 = getContinentSample2();
        assertThat(continent1).isNotEqualTo(continent2);
    }

    @Test
    void regionTest() {
        Continent continent = getContinentRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        continent.addRegion(regionBack);
        assertThat(continent.getRegions()).containsOnly(regionBack);
        assertThat(regionBack.getContinent()).isEqualTo(continent);

        continent.removeRegion(regionBack);
        assertThat(continent.getRegions()).doesNotContain(regionBack);
        assertThat(regionBack.getContinent()).isNull();

        continent.regions(new HashSet<>(Set.of(regionBack)));
        assertThat(continent.getRegions()).containsOnly(regionBack);
        assertThat(regionBack.getContinent()).isEqualTo(continent);

        continent.setRegions(new HashSet<>());
        assertThat(continent.getRegions()).doesNotContain(regionBack);
        assertThat(regionBack.getContinent()).isNull();
    }
}
