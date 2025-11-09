package io.santhoshkani.learn.mariadb.domain;

import static io.santhoshkani.learn.mariadb.domain.CountryTestSamples.*;
import static io.santhoshkani.learn.mariadb.domain.LanguageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Language.class);
        Language language1 = getLanguageSample1();
        Language language2 = new Language();
        assertThat(language1).isNotEqualTo(language2);

        language2.setId(language1.getId());
        assertThat(language1).isEqualTo(language2);

        language2 = getLanguageSample2();
        assertThat(language1).isNotEqualTo(language2);
    }

    @Test
    void countryTest() {
        Language language = getLanguageRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        language.addCountry(countryBack);
        assertThat(language.getCountries()).containsOnly(countryBack);
        assertThat(countryBack.getLanguages()).containsOnly(language);

        language.removeCountry(countryBack);
        assertThat(language.getCountries()).doesNotContain(countryBack);
        assertThat(countryBack.getLanguages()).doesNotContain(language);

        language.countries(new HashSet<>(Set.of(countryBack)));
        assertThat(language.getCountries()).containsOnly(countryBack);
        assertThat(countryBack.getLanguages()).containsOnly(language);

        language.setCountries(new HashSet<>());
        assertThat(language.getCountries()).doesNotContain(countryBack);
        assertThat(countryBack.getLanguages()).doesNotContain(language);
    }
}
