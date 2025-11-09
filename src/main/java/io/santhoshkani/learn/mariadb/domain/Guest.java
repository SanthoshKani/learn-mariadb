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
 * A Guest.
 */
@Entity
@Table(name = "guest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "guests")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "guests", "events" }, allowSetters = true)
    private Set<Vip> vips = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "guests")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vips", "guests" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Guest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Guest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Vip> getVips() {
        return this.vips;
    }

    public void setVips(Set<Vip> vips) {
        if (this.vips != null) {
            this.vips.forEach(i -> i.removeGuest(this));
        }
        if (vips != null) {
            vips.forEach(i -> i.addGuest(this));
        }
        this.vips = vips;
    }

    public Guest vips(Set<Vip> vips) {
        this.setVips(vips);
        return this;
    }

    public Guest addVip(Vip vip) {
        this.vips.add(vip);
        vip.getGuests().add(this);
        return this;
    }

    public Guest removeVip(Vip vip) {
        this.vips.remove(vip);
        vip.getGuests().remove(this);
        return this;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.removeGuest(this));
        }
        if (events != null) {
            events.forEach(i -> i.addGuest(this));
        }
        this.events = events;
    }

    public Guest events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public Guest addEvent(Event event) {
        this.events.add(event);
        event.getGuests().add(this);
        return this;
    }

    public Guest removeEvent(Event event) {
        this.events.remove(event);
        event.getGuests().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Guest)) {
            return false;
        }
        return getId() != null && getId().equals(((Guest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Guest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
