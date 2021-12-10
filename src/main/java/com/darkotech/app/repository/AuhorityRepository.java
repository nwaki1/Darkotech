package com.darkotech.app.repository;

import com.darkotech.app.domain.Auhority;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Auhority entity.
 */
@Repository
public interface AuhorityRepository extends JpaRepository<Auhority, Long> {
    @Query(
        value = "select distinct auhority from Auhority auhority left join fetch auhority.permissions",
        countQuery = "select count(distinct auhority) from Auhority auhority"
    )
    Page<Auhority> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct auhority from Auhority auhority left join fetch auhority.permissions")
    List<Auhority> findAllWithEagerRelationships();

    @Query("select auhority from Auhority auhority left join fetch auhority.permissions where auhority.id =:id")
    Optional<Auhority> findOneWithEagerRelationships(@Param("id") Long id);
}
