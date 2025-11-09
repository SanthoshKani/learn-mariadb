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
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "vips", source = "vips", qualifiedByName = "vipNameSet")
    @Mapping(target = "guests", source = "guests", qualifiedByName = "guestNameSet")
    EventDTO toDto(Event s);

    @Mapping(target = "removeVip", ignore = true)
    @Mapping(target = "removeGuest", ignore = true)
    Event toEntity(EventDTO eventDTO);

    @Named("vipName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VipDTO toDtoVipName(Vip vip);

    @Named("vipNameSet")
    default Set<VipDTO> toDtoVipNameSet(Set<Vip> vip) {
        return vip.stream().map(this::toDtoVipName).collect(Collectors.toSet());
    }

    @Named("guestName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GuestDTO toDtoGuestName(Guest guest);

    @Named("guestNameSet")
    default Set<GuestDTO> toDtoGuestNameSet(Set<Guest> guest) {
        return guest.stream().map(this::toDtoGuestName).collect(Collectors.toSet());
    }
}
