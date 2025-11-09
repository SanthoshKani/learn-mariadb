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
 * Mapper for the entity {@link Guest} and its DTO {@link GuestDTO}.
 */
@Mapper(componentModel = "spring")
public interface GuestMapper extends EntityMapper<GuestDTO, Guest> {
    @Mapping(target = "vips", source = "vips", qualifiedByName = "vipNameSet")
    @Mapping(target = "events", source = "events", qualifiedByName = "eventNameSet")
    GuestDTO toDto(Guest s);

    @Mapping(target = "vips", ignore = true)
    @Mapping(target = "removeVip", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "removeEvent", ignore = true)
    Guest toEntity(GuestDTO guestDTO);

    @Named("vipName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VipDTO toDtoVipName(Vip vip);

    @Named("vipNameSet")
    default Set<VipDTO> toDtoVipNameSet(Set<Vip> vip) {
        return vip.stream().map(this::toDtoVipName).collect(Collectors.toSet());
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
