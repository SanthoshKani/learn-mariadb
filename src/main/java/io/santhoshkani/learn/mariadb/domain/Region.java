package io.santhoshkani.learn.mariadb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countryStats", "languages", "region" }, allowSetters = true)
    private Set<Country> countries = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "regions" }, allowSetters = true)
    private Continent continent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Region id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Region name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Country> getCountries() {
        return this.countries;
    }

    public void setCountries(Set<Country> countries) {
        if (this.countries != null) {
            this.countries.forEach(i -> i.setRegion(null));
        }
        if (countries != null) {
            countries.forEach(i -> i.setRegion(this));
        }
        this.countries = countries;
    }

    public Region countries(Set<Country> countries) {
        this.setCountries(countries);
        return this;
    }

    public Region addCountry(Country country) {
        this.countries.add(country);
        country.setRegion(this);
        return this;
    }

    public Region removeCountry(Country country) {
        this.countries.remove(country);
        country.setRegion(null);
        return this;
    }

    public Continent getContinent() {
        return this.continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public Region continent(Continent continent) {
        this.setContinent(continent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return getId() != null && getId().equals(((Region) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Region{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
