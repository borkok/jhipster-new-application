package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterNewApplicationApp;
import io.github.jhipster.application.domain.Execution;
import io.github.jhipster.application.repository.ExecutionRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ExecutionResource} REST controller.
 */
@SpringBootTest(classes = JhipsterNewApplicationApp.class)
public class ExecutionResourceIT {

    private static final LocalDate DEFAULT_PLANNED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PLANNED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DONE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DONE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DONE_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_DONE_COMMENT = "BBBBBBBBBB";

    @Autowired
    private ExecutionRepository executionRepository;

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

    private MockMvc restExecutionMockMvc;

    private Execution execution;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExecutionResource executionResource = new ExecutionResource(executionRepository);
        this.restExecutionMockMvc = MockMvcBuilders.standaloneSetup(executionResource)
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
    public static Execution createEntity(EntityManager em) {
        Execution execution = new Execution()
            .planned(DEFAULT_PLANNED)
            .done(DEFAULT_DONE)
            .doneComment(DEFAULT_DONE_COMMENT);
        return execution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Execution createUpdatedEntity(EntityManager em) {
        Execution execution = new Execution()
            .planned(UPDATED_PLANNED)
            .done(UPDATED_DONE)
            .doneComment(UPDATED_DONE_COMMENT);
        return execution;
    }

    @BeforeEach
    public void initTest() {
        execution = createEntity(em);
    }

    @Test
    @Transactional
    public void createExecution() throws Exception {
        int databaseSizeBeforeCreate = executionRepository.findAll().size();

        // Create the Execution
        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execution)))
            .andExpect(status().isCreated());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeCreate + 1);
        Execution testExecution = executionList.get(executionList.size() - 1);
        assertThat(testExecution.getPlanned()).isEqualTo(DEFAULT_PLANNED);
        assertThat(testExecution.getDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testExecution.getDoneComment()).isEqualTo(DEFAULT_DONE_COMMENT);
    }

    @Test
    @Transactional
    public void createExecutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = executionRepository.findAll().size();

        // Create the Execution with an existing ID
        execution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execution)))
            .andExpect(status().isBadRequest());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPlannedIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionRepository.findAll().size();
        // set the field null
        execution.setPlanned(null);

        // Create the Execution, which fails.

        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execution)))
            .andExpect(status().isBadRequest());

        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExecutions() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        // Get all the executionList
        restExecutionMockMvc.perform(get("/api/executions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(execution.getId().intValue())))
            .andExpect(jsonPath("$.[*].planned").value(hasItem(DEFAULT_PLANNED.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.toString())))
            .andExpect(jsonPath("$.[*].doneComment").value(hasItem(DEFAULT_DONE_COMMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        // Get the execution
        restExecutionMockMvc.perform(get("/api/executions/{id}", execution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(execution.getId().intValue()))
            .andExpect(jsonPath("$.planned").value(DEFAULT_PLANNED.toString()))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.toString()))
            .andExpect(jsonPath("$.doneComment").value(DEFAULT_DONE_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExecution() throws Exception {
        // Get the execution
        restExecutionMockMvc.perform(get("/api/executions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        int databaseSizeBeforeUpdate = executionRepository.findAll().size();

        // Update the execution
        Execution updatedExecution = executionRepository.findById(execution.getId()).get();
        // Disconnect from session so that the updates on updatedExecution are not directly saved in db
        em.detach(updatedExecution);
        updatedExecution
            .planned(UPDATED_PLANNED)
            .done(UPDATED_DONE)
            .doneComment(UPDATED_DONE_COMMENT);

        restExecutionMockMvc.perform(put("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExecution)))
            .andExpect(status().isOk());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeUpdate);
        Execution testExecution = executionList.get(executionList.size() - 1);
        assertThat(testExecution.getPlanned()).isEqualTo(UPDATED_PLANNED);
        assertThat(testExecution.getDone()).isEqualTo(UPDATED_DONE);
        assertThat(testExecution.getDoneComment()).isEqualTo(UPDATED_DONE_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingExecution() throws Exception {
        int databaseSizeBeforeUpdate = executionRepository.findAll().size();

        // Create the Execution

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutionMockMvc.perform(put("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execution)))
            .andExpect(status().isBadRequest());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        int databaseSizeBeforeDelete = executionRepository.findAll().size();

        // Delete the execution
        restExecutionMockMvc.perform(delete("/api/executions/{id}", execution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Execution.class);
        Execution execution1 = new Execution();
        execution1.setId(1L);
        Execution execution2 = new Execution();
        execution2.setId(execution1.getId());
        assertThat(execution1).isEqualTo(execution2);
        execution2.setId(2L);
        assertThat(execution1).isNotEqualTo(execution2);
        execution1.setId(null);
        assertThat(execution1).isNotEqualTo(execution2);
    }
}
