package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.HowOften;
import io.github.jhipster.application.repository.HowOftenRepository;
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
 * REST controller for managing {@link io.github.jhipster.application.domain.HowOften}.
 */
@RestController
@RequestMapping("/api")
public class HowOftenResource {

    private final Logger log = LoggerFactory.getLogger(HowOftenResource.class);

    private static final String ENTITY_NAME = "howOften";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HowOftenRepository howOftenRepository;

    public HowOftenResource(HowOftenRepository howOftenRepository) {
        this.howOftenRepository = howOftenRepository;
    }

    /**
     * {@code POST  /how-oftens} : Create a new howOften.
     *
     * @param howOften the howOften to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new howOften, or with status {@code 400 (Bad Request)} if the howOften has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/how-oftens")
    public ResponseEntity<HowOften> createHowOften(@Valid @RequestBody HowOften howOften) throws URISyntaxException {
        log.debug("REST request to save HowOften : {}", howOften);
        if (howOften.getId() != null) {
            throw new BadRequestAlertException("A new howOften cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HowOften result = howOftenRepository.save(howOften);
        return ResponseEntity.created(new URI("/api/how-oftens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /how-oftens} : Updates an existing howOften.
     *
     * @param howOften the howOften to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated howOften,
     * or with status {@code 400 (Bad Request)} if the howOften is not valid,
     * or with status {@code 500 (Internal Server Error)} if the howOften couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/how-oftens")
    public ResponseEntity<HowOften> updateHowOften(@Valid @RequestBody HowOften howOften) throws URISyntaxException {
        log.debug("REST request to update HowOften : {}", howOften);
        if (howOften.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HowOften result = howOftenRepository.save(howOften);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, howOften.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /how-oftens} : get all the howOftens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of howOftens in body.
     */
    @GetMapping("/how-oftens")
    public List<HowOften> getAllHowOftens() {
        log.debug("REST request to get all HowOftens");
        return howOftenRepository.findAll();
    }

    /**
     * {@code GET  /how-oftens/:id} : get the "id" howOften.
     *
     * @param id the id of the howOften to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the howOften, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/how-oftens/{id}")
    public ResponseEntity<HowOften> getHowOften(@PathVariable Long id) {
        log.debug("REST request to get HowOften : {}", id);
        Optional<HowOften> howOften = howOftenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(howOften);
    }

    /**
     * {@code DELETE  /how-oftens/:id} : delete the "id" howOften.
     *
     * @param id the id of the howOften to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/how-oftens/{id}")
    public ResponseEntity<Void> deleteHowOften(@PathVariable Long id) {
        log.debug("REST request to delete HowOften : {}", id);
        howOftenRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
