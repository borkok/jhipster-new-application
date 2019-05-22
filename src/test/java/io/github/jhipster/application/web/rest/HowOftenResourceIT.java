package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterNewApplicationApp;
import io.github.jhipster.application.domain.HowOften;
import io.github.jhipster.application.repository.HowOftenRepository;
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

import io.github.jhipster.application.domain.enumeration.Unit;
/**
 * Integration tests for the {@Link HowOftenResource} REST controller.
 */
@SpringBootTest(classes = JhipsterNewApplicationApp.class)
public class HowOftenResourceIT {

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Unit DEFAULT_UNIT = Unit.DAYS;
    private static final Unit UPDATED_UNIT = Unit.DAYS;

    @Autowired
    private HowOftenRepository howOftenRepository;

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

    private MockMvc restHowOftenMockMvc;

    private HowOften howOften;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HowOftenResource howOftenResource = new HowOftenResource(howOftenRepository);
        this.restHowOftenMockMvc = MockMvcBuilders.standaloneSetup(howOftenResource)
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
    public static HowOften createEntity(EntityManager em) {
        HowOften howOften = new HowOften()
            .count(DEFAULT_COUNT)
            .unit(DEFAULT_UNIT);
        return howOften;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HowOften createUpdatedEntity(EntityManager em) {
        HowOften howOften = new HowOften()
            .count(UPDATED_COUNT)
            .unit(UPDATED_UNIT);
        return howOften;
    }

    @BeforeEach
    public void initTest() {
        howOften = createEntity(em);
    }

    @Test
    @Transactional
    public void createHowOften() throws Exception {
        int databaseSizeBeforeCreate = howOftenRepository.findAll().size();

        // Create the HowOften
        restHowOftenMockMvc.perform(post("/api/how-oftens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(howOften)))
            .andExpect(status().isCreated());

        // Validate the HowOften in the database
        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeCreate + 1);
        HowOften testHowOften = howOftenList.get(howOftenList.size() - 1);
        assertThat(testHowOften.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testHowOften.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void createHowOftenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = howOftenRepository.findAll().size();

        // Create the HowOften with an existing ID
        howOften.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHowOftenMockMvc.perform(post("/api/how-oftens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(howOften)))
            .andExpect(status().isBadRequest());

        // Validate the HowOften in the database
        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = howOftenRepository.findAll().size();
        // set the field null
        howOften.setCount(null);

        // Create the HowOften, which fails.

        restHowOftenMockMvc.perform(post("/api/how-oftens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(howOften)))
            .andExpect(status().isBadRequest());

        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = howOftenRepository.findAll().size();
        // set the field null
        howOften.setUnit(null);

        // Create the HowOften, which fails.

        restHowOftenMockMvc.perform(post("/api/how-oftens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(howOften)))
            .andExpect(status().isBadRequest());

        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHowOftens() throws Exception {
        // Initialize the database
        howOftenRepository.saveAndFlush(howOften);

        // Get all the howOftenList
        restHowOftenMockMvc.perform(get("/api/how-oftens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(howOften.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }
    
    @Test
    @Transactional
    public void getHowOften() throws Exception {
        // Initialize the database
        howOftenRepository.saveAndFlush(howOften);

        // Get the howOften
        restHowOftenMockMvc.perform(get("/api/how-oftens/{id}", howOften.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(howOften.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHowOften() throws Exception {
        // Get the howOften
        restHowOftenMockMvc.perform(get("/api/how-oftens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHowOften() throws Exception {
        // Initialize the database
        howOftenRepository.saveAndFlush(howOften);

        int databaseSizeBeforeUpdate = howOftenRepository.findAll().size();

        // Update the howOften
        HowOften updatedHowOften = howOftenRepository.findById(howOften.getId()).get();
        // Disconnect from session so that the updates on updatedHowOften are not directly saved in db
        em.detach(updatedHowOften);
        updatedHowOften
            .count(UPDATED_COUNT)
            .unit(UPDATED_UNIT);

        restHowOftenMockMvc.perform(put("/api/how-oftens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHowOften)))
            .andExpect(status().isOk());

        // Validate the HowOften in the database
        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeUpdate);
        HowOften testHowOften = howOftenList.get(howOftenList.size() - 1);
        assertThat(testHowOften.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testHowOften.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void updateNonExistingHowOften() throws Exception {
        int databaseSizeBeforeUpdate = howOftenRepository.findAll().size();

        // Create the HowOften

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHowOftenMockMvc.perform(put("/api/how-oftens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(howOften)))
            .andExpect(status().isBadRequest());

        // Validate the HowOften in the database
        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHowOften() throws Exception {
        // Initialize the database
        howOftenRepository.saveAndFlush(howOften);

        int databaseSizeBeforeDelete = howOftenRepository.findAll().size();

        // Delete the howOften
        restHowOftenMockMvc.perform(delete("/api/how-oftens/{id}", howOften.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<HowOften> howOftenList = howOftenRepository.findAll();
        assertThat(howOftenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HowOften.class);
        HowOften howOften1 = new HowOften();
        howOften1.setId(1L);
        HowOften howOften2 = new HowOften();
        howOften2.setId(howOften1.getId());
        assertThat(howOften1).isEqualTo(howOften2);
        howOften2.setId(2L);
        assertThat(howOften1).isNotEqualTo(howOften2);
        howOften1.setId(null);
        assertThat(howOften1).isNotEqualTo(howOften2);
    }
}
