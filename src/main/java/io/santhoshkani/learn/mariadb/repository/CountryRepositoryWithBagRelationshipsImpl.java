package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Country;
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
public class CountryRepositoryWithBagRelationshipsImpl implements CountryRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String COUNTRIES_PARAMETER = "countries";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Country> fetchBagRelationships(Optional<Country> country) {
        return country.map(this::fetchLanguages);
    }

    @Override
    public Page<Country> fetchBagRelationships(Page<Country> countries) {
        return new PageImpl<>(fetchBagRelationships(countries.getContent()), countries.getPageable(), countries.getTotalElements());
    }

    @Override
    public List<Country> fetchBagRelationships(List<Country> countries) {
        return Optional.of(countries).map(this::fetchLanguages).orElse(Collections.emptyList());
    }

    Country fetchLanguages(Country result) {
        return entityManager
            .createQuery("select country from Country country left join fetch country.languages where country.id = :id", Country.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Country> fetchLanguages(List<Country> countries) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, countries.size()).forEach(index -> order.put(countries.get(index).getId(), index));
        List<Country> result = entityManager
            .createQuery("select country from Country country left join fetch country.languages where country in :countries", Country.class)
            .setParameter(COUNTRIES_PARAMETER, countries)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
