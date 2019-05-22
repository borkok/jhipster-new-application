package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Execution;
import io.github.jhipster.application.repository.ExecutionRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.application.domain.Execution}.
 */
@RestController
@RequestMapping("/api")
public class ExecutionResource {

    private final Logger log = LoggerFactory.getLogger(ExecutionResource.class);

    private static final String ENTITY_NAME = "execution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExecutionRepository executionRepository;

    public ExecutionResource(ExecutionRepository executionRepository) {
        this.executionRepository = executionRepository;
    }

    /**
     * {@code POST  /executions} : Create a new execution.
     *
     * @param execution the execution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new execution, or with status {@code 400 (Bad Request)} if the execution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/executions")
    public ResponseEntity<Execution> createExecution(@Valid @RequestBody Execution execution) throws URISyntaxException {
        log.debug("REST request to save Execution : {}", execution);
        if (execution.getId() != null) {
            throw new BadRequestAlertException("A new execution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Execution result = executionRepository.save(execution);
        return ResponseEntity.created(new URI("/api/executions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /executions} : Updates an existing execution.
     *
     * @param execution the execution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated execution,
     * or with status {@code 400 (Bad Request)} if the execution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the execution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/executions")
    public ResponseEntity<Execution> updateExecution(@Valid @RequestBody Execution execution) throws URISyntaxException {
        log.debug("REST request to update Execution : {}", execution);
        if (execution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Execution result = executionRepository.save(execution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, execution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /executions} : get all the executions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of executions in body.
     */
    @GetMapping("/executions")
    public List<Execution> getAllExecutions() {
        log.debug("REST request to get all Executions");
        return executionRepository.findAll();
    }

    /**
     * {@code GET  /executions/:id} : get the "id" execution.
     *
     * @param id the id of the execution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the execution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/executions/{id}")
    public ResponseEntity<Execution> getExecution(@PathVariable Long id) {
        log.debug("REST request to get Execution : {}", id);
        Optional<Execution> execution = executionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(execution);
    }

    /**
     * {@code DELETE  /executions/:id} : delete the "id" execution.
     *
     * @param id the id of the execution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/executions/{id}")
    public ResponseEntity<Void> deleteExecution(@PathVariable Long id) {
        log.debug("REST request to delete Execution : {}", id);
        executionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
