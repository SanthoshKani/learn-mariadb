package io.santhoshkani.learn.mariadb.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.santhoshkani.learn.mariadb.domain.Vip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VipDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<GuestDTO> guests = new HashSet<>();

    private Set<EventDTO> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GuestDTO> getGuests() {
        return guests;
    }

    public void setGuests(Set<GuestDTO> guests) {
        this.guests = guests;
    }

    public Set<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(Set<EventDTO> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VipDTO)) {
            return false;
        }

        VipDTO vipDTO = (VipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VipDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", guests=" + getGuests() +
            ", events=" + getEvents() +
            "}";
    }
}
