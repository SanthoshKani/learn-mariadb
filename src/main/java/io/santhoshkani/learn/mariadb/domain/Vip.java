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
 * A Vip.
 */
@Entity
@Table(name = "vip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_vip__guest", joinColumns = @JoinColumn(name = "vip_id"), inverseJoinColumns = @JoinColumn(name = "guest_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vips", "events" }, allowSetters = true)
    private Set<Guest> guests = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "vips")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vips", "guests" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Vip name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Guest> getGuests() {
        return this.guests;
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public Vip guests(Set<Guest> guests) {
        this.setGuests(guests);
        return this;
    }

    public Vip addGuest(Guest guest) {
        this.guests.add(guest);
        return this;
    }

    public Vip removeGuest(Guest guest) {
        this.guests.remove(guest);
        return this;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.removeVip(this));
        }
        if (events != null) {
            events.forEach(i -> i.addVip(this));
        }
        this.events = events;
    }

    public Vip events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public Vip addEvent(Event event) {
        this.events.add(event);
        event.getVips().add(this);
        return this;
    }

    public Vip removeEvent(Event event) {
        this.events.remove(event);
        event.getVips().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vip)) {
            return false;
        }
        return getId() != null && getId().equals(((Vip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vip{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
