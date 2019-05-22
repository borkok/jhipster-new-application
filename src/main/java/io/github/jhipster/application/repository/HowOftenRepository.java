package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.HowOften;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HowOften entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HowOftenRepository extends JpaRepository<HowOften, Long> {

}
