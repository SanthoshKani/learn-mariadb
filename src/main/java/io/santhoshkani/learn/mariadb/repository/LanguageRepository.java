package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Language;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {}
