package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Diagnostic;
import io.github.jhipster.application.repository.DiagnosticRepository;
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
 * REST controller for managing {@link io.github.jhipster.application.domain.Diagnostic}.
 */
@RestController
@RequestMapping("/api")
public class DiagnosticResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticResource.class);

    private static final String ENTITY_NAME = "diagnostic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiagnosticRepository diagnosticRepository;

    public DiagnosticResource(DiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    /**
     * {@code POST  /diagnostics} : Create a new diagnostic.
     *
     * @param diagnostic the diagnostic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnostic, or with status {@code 400 (Bad Request)} if the diagnostic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diagnostics")
    public ResponseEntity<Diagnostic> createDiagnostic(@Valid @RequestBody Diagnostic diagnostic) throws URISyntaxException {
        log.debug("REST request to save Diagnostic : {}", diagnostic);
        if (diagnostic.getId() != null) {
            throw new BadRequestAlertException("A new diagnostic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Diagnostic result = diagnosticRepository.save(diagnostic);
        return ResponseEntity.created(new URI("/api/diagnostics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /diagnostics} : Updates an existing diagnostic.
     *
     * @param diagnostic the diagnostic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnostic,
     * or with status {@code 400 (Bad Request)} if the diagnostic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnostic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diagnostics")
    public ResponseEntity<Diagnostic> updateDiagnostic(@Valid @RequestBody Diagnostic diagnostic) throws URISyntaxException {
        log.debug("REST request to update Diagnostic : {}", diagnostic);
        if (diagnostic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Diagnostic result = diagnosticRepository.save(diagnostic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, diagnostic.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /diagnostics} : get all the diagnostics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diagnostics in body.
     */
    @GetMapping("/diagnostics")
    public List<Diagnostic> getAllDiagnostics() {
        log.debug("REST request to get all Diagnostics");
        return diagnosticRepository.findAll();
    }

    /**
     * {@code GET  /diagnostics/:id} : get the "id" diagnostic.
     *
     * @param id the id of the diagnostic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diagnostic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diagnostics/{id}")
    public ResponseEntity<Diagnostic> getDiagnostic(@PathVariable Long id) {
        log.debug("REST request to get Diagnostic : {}", id);
        Optional<Diagnostic> diagnostic = diagnosticRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(diagnostic);
    }

    /**
     * {@code DELETE  /diagnostics/:id} : delete the "id" diagnostic.
     *
     * @param id the id of the diagnostic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diagnostics/{id}")
    public ResponseEntity<Void> deleteDiagnostic(@PathVariable Long id) {
        log.debug("REST request to delete Diagnostic : {}", id);
        diagnosticRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
