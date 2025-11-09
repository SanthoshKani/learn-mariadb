package io.santhoshkani.learn.mariadb.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.santhoshkani.learn.mariadb.domain.Guest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GuestDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<VipDTO> vips = new HashSet<>();

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

    public Set<VipDTO> getVips() {
        return vips;
    }

    public void setVips(Set<VipDTO> vips) {
        this.vips = vips;
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
        if (!(o instanceof GuestDTO)) {
            return false;
        }

        GuestDTO guestDTO = (GuestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, guestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", vips=" + getVips() +
            ", events=" + getEvents() +
            "}";
    }
}
