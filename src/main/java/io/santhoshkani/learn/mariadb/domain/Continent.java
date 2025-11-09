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
 * A Continent.
 */
@Entity
@Table(name = "continent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "continent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countries", "continent" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Continent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Continent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Region> getRegions() {
        return this.regions;
    }

    public void setRegions(Set<Region> regions) {
        if (this.regions != null) {
            this.regions.forEach(i -> i.setContinent(null));
        }
        if (regions != null) {
            regions.forEach(i -> i.setContinent(this));
        }
        this.regions = regions;
    }

    public Continent regions(Set<Region> regions) {
        this.setRegions(regions);
        return this;
    }

    public Continent addRegion(Region region) {
        this.regions.add(region);
        region.setContinent(this);
        return this;
    }

    public Continent removeRegion(Region region) {
        this.regions.remove(region);
        region.setContinent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Continent)) {
            return false;
        }
        return getId() != null && getId().equals(((Continent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Continent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
