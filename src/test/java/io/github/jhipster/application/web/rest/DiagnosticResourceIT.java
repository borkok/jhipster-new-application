package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterNewApplicationApp;
import io.github.jhipster.application.domain.Diagnostic;
import io.github.jhipster.application.repository.DiagnosticRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link DiagnosticResource} REST controller.
 */
@SpringBootTest(classes = JhipsterNewApplicationApp.class)
public class DiagnosticResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DiagnosticRepository diagnosticRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDiagnosticMockMvc;

    private Diagnostic diagnostic;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiagnosticResource diagnosticResource = new DiagnosticResource(diagnosticRepository);
        this.restDiagnosticMockMvc = MockMvcBuilders.standaloneSetup(diagnosticResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostic createEntity(EntityManager em) {
        Diagnostic diagnostic = new Diagnostic()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return diagnostic;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostic createUpdatedEntity(EntityManager em) {
        Diagnostic diagnostic = new Diagnostic()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return diagnostic;
    }

    @BeforeEach
    public void initTest() {
        diagnostic = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiagnostic() throws Exception {
        int databaseSizeBeforeCreate = diagnosticRepository.findAll().size();

        // Create the Diagnostic
        restDiagnosticMockMvc.perform(post("/api/diagnostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
            .andExpect(status().isCreated());

        // Validate the Diagnostic in the database
        List<Diagnostic> diagnosticList = diagnosticRepository.findAll();
        assertThat(diagnosticList).hasSize(databaseSizeBeforeCreate + 1);
        Diagnostic testDiagnostic = diagnosticList.get(diagnosticList.size() - 1);
        assertThat(testDiagnostic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDiagnostic.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDiagnosticWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = diagnosticRepository.findAll().size();

        // Create the Diagnostic with an existing ID
        diagnostic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiagnosticMockMvc.perform(post("/api/diagnostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        List<Diagnostic> diagnosticList = diagnosticRepository.findAll();
        assertThat(diagnosticList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticRepository.findAll().size();
        // set the field null
        diagnostic.setName(null);

        // Create the Diagnostic, which fails.

        restDiagnosticMockMvc.perform(post("/api/diagnostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
            .andExpect(status().isBadRequest());

        List<Diagnostic> diagnosticList = diagnosticRepository.findAll();
        assertThat(diagnosticList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiagnostics() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList
        restDiagnosticMockMvc.perform(get("/api/diagnostics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get the diagnostic
        restDiagnosticMockMvc.perform(get("/api/diagnostics/{id}", diagnostic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diagnostic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiagnostic() throws Exception {
        // Get the diagnostic
        restDiagnosticMockMvc.perform(get("/api/diagnostics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        int databaseSizeBeforeUpdate = diagnosticRepository.findAll().size();

        // Update the diagnostic
        Diagnostic updatedDiagnostic = diagnosticRepository.findById(diagnostic.getId()).get();
        // Disconnect from session so that the updates on updatedDiagnostic are not directly saved in db
        em.detach(updatedDiagnostic);
        updatedDiagnostic
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restDiagnosticMockMvc.perform(put("/api/diagnostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiagnostic)))
            .andExpect(status().isOk());

        // Validate the Diagnostic in the database
        List<Diagnostic> diagnosticList = diagnosticRepository.findAll();
        assertThat(diagnosticList).hasSize(databaseSizeBeforeUpdate);
        Diagnostic testDiagnostic = diagnosticList.get(diagnosticList.size() - 1);
        assertThat(testDiagnostic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDiagnostic.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDiagnostic() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticRepository.findAll().size();

        // Create the Diagnostic

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc.perform(put("/api/diagnostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        List<Diagnostic> diagnosticList = diagnosticRepository.findAll();
        assertThat(diagnosticList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        int databaseSizeBeforeDelete = diagnosticRepository.findAll().size();

        // Delete the diagnostic
        restDiagnosticMockMvc.perform(delete("/api/diagnostics/{id}", diagnostic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Diagnostic> diagnosticList = diagnosticRepository.findAll();
        assertThat(diagnosticList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diagnostic.class);
        Diagnostic diagnostic1 = new Diagnostic();
        diagnostic1.setId(1L);
        Diagnostic diagnostic2 = new Diagnostic();
        diagnostic2.setId(diagnostic1.getId());
        assertThat(diagnostic1).isEqualTo(diagnostic2);
        diagnostic2.setId(2L);
        assertThat(diagnostic1).isNotEqualTo(diagnostic2);
        diagnostic1.setId(null);
        assertThat(diagnostic1).isNotEqualTo(diagnostic2);
    }
}
