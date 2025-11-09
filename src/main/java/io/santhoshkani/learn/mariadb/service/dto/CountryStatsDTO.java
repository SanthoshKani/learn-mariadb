package io.santhoshkani.learn.mariadb.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link io.santhoshkani.learn.mariadb.domain.CountryStats} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryStatsDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer year;

    private Long population;

    private BigDecimal gdp;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public BigDecimal getGdp() {
        return gdp;
    }

    public void setGdp(BigDecimal gdp) {
        this.gdp = gdp;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryStatsDTO)) {
            return false;
        }

        CountryStatsDTO countryStatsDTO = (CountryStatsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryStatsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryStatsDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", population=" + getPopulation() +
            ", gdp=" + getGdp() +
            ", country=" + getCountry() +
            "}";
    }
}
