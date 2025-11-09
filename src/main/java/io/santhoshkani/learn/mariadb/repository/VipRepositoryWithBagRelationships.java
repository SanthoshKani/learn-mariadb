package io.santhoshkani.learn.mariadb.repository;

import io.santhoshkani.learn.mariadb.domain.Vip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VipRepositoryWithBagRelationships {
    Optional<Vip> fetchBagRelationships(Optional<Vip> vip);

    List<Vip> fetchBagRelationships(List<Vip> vips);

    Page<Vip> fetchBagRelationships(Page<Vip> vips);
}
