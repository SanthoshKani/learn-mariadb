package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Vip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class VipRepositoryWithBagRelationshipsImpl implements VipRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String VIPS_PARAMETER = "vips";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Vip> fetchBagRelationships(Optional<Vip> vip) {
        return vip.map(this::fetchGuests);
    }

    @Override
    public Page<Vip> fetchBagRelationships(Page<Vip> vips) {
        return new PageImpl<>(fetchBagRelationships(vips.getContent()), vips.getPageable(), vips.getTotalElements());
    }

    @Override
    public List<Vip> fetchBagRelationships(List<Vip> vips) {
        return Optional.of(vips).map(this::fetchGuests).orElse(Collections.emptyList());
    }

    Vip fetchGuests(Vip result) {
        return entityManager
            .createQuery("select vip from Vip vip left join fetch vip.guests where vip.id = :id", Vip.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Vip> fetchGuests(List<Vip> vips) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vips.size()).forEach(index -> order.put(vips.get(index).getId(), index));
        List<Vip> result = entityManager
            .createQuery("select vip from Vip vip left join fetch vip.guests where vip in :vips", Vip.class)
            .setParameter(VIPS_PARAMETER, vips)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
