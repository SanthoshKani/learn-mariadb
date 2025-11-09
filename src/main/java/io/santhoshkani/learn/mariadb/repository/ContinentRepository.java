package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Continent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Continent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContinentRepository extends JpaRepository<Continent, Long> {}
