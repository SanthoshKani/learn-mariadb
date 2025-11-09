package io.santhoshkani.learn.mariadb.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.santhoshkani.learn.mariadb.domain.Event} entity.
 */
@Schema(description = "Additional entity connecting VIPs and Guests for events")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant date;

    private Set<VipDTO> vips = new HashSet<>();

    private Set<GuestDTO> guests = new HashSet<>();

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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<VipDTO> getVips() {
        return vips;
    }

    public void setVips(Set<VipDTO> vips) {
        this.vips = vips;
    }

    public Set<GuestDTO> getGuests() {
        return guests;
    }

    public void setGuests(Set<GuestDTO> guests) {
        this.guests = guests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDTO)) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", vips=" + getVips() +
            ", guests=" + getGuests() +
            "}";
    }
}
