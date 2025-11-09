package io.santhoshkani.learn.mariadb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Additional entity connecting VIPs and Guests for events
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event implements Serializable {

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
    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_event__vip", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "vip_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "guests", "events" }, allowSetters = true)
    private Set<Vip> vips = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_event__guest", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "guest_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vips", "events" }, allowSetters = true)
    private Set<Guest> guests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Event name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return this.date;
    }

    public Event date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<Vip> getVips() {
        return this.vips;
    }

    public void setVips(Set<Vip> vips) {
        this.vips = vips;
    }

    public Event vips(Set<Vip> vips) {
        this.setVips(vips);
        return this;
    }

    public Event addVip(Vip vip) {
        this.vips.add(vip);
        return this;
    }

    public Event removeVip(Vip vip) {
        this.vips.remove(vip);
        return this;
    }

    public Set<Guest> getGuests() {
        return this.guests;
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public Event guests(Set<Guest> guests) {
        this.setGuests(guests);
        return this;
    }

    public Event addGuest(Guest guest) {
        this.guests.add(guest);
        return this;
    }

    public Event removeGuest(Guest guest) {
        this.guests.remove(guest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return getId() != null && getId().equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
