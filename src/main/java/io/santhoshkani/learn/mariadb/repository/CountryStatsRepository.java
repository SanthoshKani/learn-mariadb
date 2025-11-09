package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.CountryStats;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CountryStats entity.
 */
@Repository
public interface CountryStatsRepository extends JpaRepository<CountryStats, Long> {
    default Optional<CountryStats> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CountryStats> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CountryStats> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select countryStats from CountryStats countryStats left join fetch countryStats.country",
        countQuery = "select count(countryStats) from CountryStats countryStats"
    )
    Page<CountryStats> findAllWithToOneRelationships(Pageable pageable);

    @Query("select countryStats from CountryStats countryStats left join fetch countryStats.country")
    List<CountryStats> findAllWithToOneRelationships();

    @Query("select countryStats from CountryStats countryStats left join fetch countryStats.country where countryStats.id =:id")
    Optional<CountryStats> findOneWithToOneRelationships(@Param("id") Long id);
}
