package io.santhoshkani.learn.mariadb.service.mapper;

import io.santhoshkani.learn.mariadb.domain.Event;
import io.santhoshkani.learn.mariadb.domain.Guest;
import io.santhoshkani.learn.mariadb.domain.Vip;
import io.santhoshkani.learn.mariadb.service.dto.EventDTO;
import io.santhoshkani.learn.mariadb.service.dto.GuestDTO;
import io.santhoshkani.learn.mariadb.service.dto.VipDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vip} and its DTO {@link VipDTO}.
 */
@Mapper(componentModel = "spring")
public interface VipMapper extends EntityMapper<VipDTO, Vip> {
    @Mapping(target = "guests", source = "guests", qualifiedByName = "guestNameSet")
    @Mapping(target = "events", source = "events", qualifiedByName = "eventNameSet")
    VipDTO toDto(Vip s);

    @Mapping(target = "removeGuest", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "removeEvent", ignore = true)
    Vip toEntity(VipDTO vipDTO);

    @Named("guestName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GuestDTO toDtoGuestName(Guest guest);

    @Named("guestNameSet")
    default Set<GuestDTO> toDtoGuestNameSet(Set<Guest> guest) {
        return guest.stream().map(this::toDtoGuestName).collect(Collectors.toSet());
    }

    @Named("eventName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EventDTO toDtoEventName(Event event);

    @Named("eventNameSet")
    default Set<EventDTO> toDtoEventNameSet(Set<Event> event) {
        return event.stream().map(this::toDtoEventName).collect(Collectors.toSet());
    }
}
