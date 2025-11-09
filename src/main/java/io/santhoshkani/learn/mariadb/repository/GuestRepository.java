package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Guest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Guest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {}
