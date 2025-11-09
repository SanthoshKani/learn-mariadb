package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Region;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Region entity.
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    default Optional<Region> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Region> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Region> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select region from Region region left join fetch region.continent",
        countQuery = "select count(region) from Region region"
    )
    Page<Region> findAllWithToOneRelationships(Pageable pageable);

    @Query("select region from Region region left join fetch region.continent")
    List<Region> findAllWithToOneRelationships();

    @Query("select region from Region region left join fetch region.continent where region.id =:id")
    Optional<Region> findOneWithToOneRelationships(@Param("id") Long id);
}
