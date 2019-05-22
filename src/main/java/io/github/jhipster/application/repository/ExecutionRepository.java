package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Execution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Execution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

}
