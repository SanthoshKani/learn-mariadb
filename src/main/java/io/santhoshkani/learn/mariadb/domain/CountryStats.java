package io.santhoshkani.learn.mariadb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountryStats.
 */
@Entity
@Table(name = "country_stats")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "population")
    private Long population;

    @Column(name = "gdp", precision = 21, scale = 2)
    private BigDecimal gdp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countryStats", "languages", "region" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountryStats id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public CountryStats year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getPopulation() {
        return this.population;
    }

    public CountryStats population(Long population) {
        this.setPopulation(population);
        return this;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public BigDecimal getGdp() {
        return this.gdp;
    }

    public CountryStats gdp(BigDecimal gdp) {
        this.setGdp(gdp);
        return this;
    }

    public void setGdp(BigDecimal gdp) {
        this.gdp = gdp;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public CountryStats country(Country country) {
        this.setCountry(country);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryStats)) {
            return false;
        }
        return getId() != null && getId().equals(((CountryStats) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryStats{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", population=" + getPopulation() +
            ", gdp=" + getGdp() +
            "}";
    }
}
