package io.santhoshkani.learn.mariadb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "area", precision = 21, scale = 2, nullable = false)
    private BigDecimal area;

    @Column(name = "national_day")
    private LocalDate nationalDay;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "country_code_2", length = 2, nullable = false)
    private String countryCode2;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "country_code_3", length = 3, nullable = false)
    private String countryCode3;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private Set<CountryStats> countryStats = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_country__language",
        joinColumns = @JoinColumn(name = "country_id"),
        inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    private Set<Language> languages = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countries", "continent" }, allowSetters = true)
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getArea() {
        return this.area;
    }

    public Country area(BigDecimal area) {
        this.setArea(area);
        return this;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public LocalDate getNationalDay() {
        return this.nationalDay;
    }

    public Country nationalDay(LocalDate nationalDay) {
        this.setNationalDay(nationalDay);
        return this;
    }

    public void setNationalDay(LocalDate nationalDay) {
        this.nationalDay = nationalDay;
    }

    public String getCountryCode2() {
        return this.countryCode2;
    }

    public Country countryCode2(String countryCode2) {
        this.setCountryCode2(countryCode2);
        return this;
    }

    public void setCountryCode2(String countryCode2) {
        this.countryCode2 = countryCode2;
    }

    public String getCountryCode3() {
        return this.countryCode3;
    }

    public Country countryCode3(String countryCode3) {
        this.setCountryCode3(countryCode3);
        return this;
    }

    public void setCountryCode3(String countryCode3) {
        this.countryCode3 = countryCode3;
    }

    public Set<CountryStats> getCountryStats() {
        return this.countryStats;
    }

    public void setCountryStats(Set<CountryStats> countryStats) {
        if (this.countryStats != null) {
            this.countryStats.forEach(i -> i.setCountry(null));
        }
        if (countryStats != null) {
            countryStats.forEach(i -> i.setCountry(this));
        }
        this.countryStats = countryStats;
    }

    public Country countryStats(Set<CountryStats> countryStats) {
        this.setCountryStats(countryStats);
        return this;
    }

    public Country addCountryStat(CountryStats countryStats) {
        this.countryStats.add(countryStats);
        countryStats.setCountry(this);
        return this;
    }

    public Country removeCountryStat(CountryStats countryStats) {
        this.countryStats.remove(countryStats);
        countryStats.setCountry(null);
        return this;
    }

    public Set<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Country languages(Set<Language> languages) {
        this.setLanguages(languages);
        return this;
    }

    public Country addLanguage(Language language) {
        this.languages.add(language);
        return this;
    }

    public Country removeLanguage(Language language) {
        this.languages.remove(language);
        return this;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Country region(Region region) {
        this.setRegion(region);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", area=" + getArea() +
            ", nationalDay='" + getNationalDay() + "'" +
            ", countryCode2='" + getCountryCode2() + "'" +
            ", countryCode3='" + getCountryCode3() + "'" +
            "}";
    }
}
