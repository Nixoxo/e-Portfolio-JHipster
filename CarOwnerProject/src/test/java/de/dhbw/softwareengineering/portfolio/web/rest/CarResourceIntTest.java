package de.dhbw.softwareengineering.portfolio.web.rest;

import de.dhbw.softwareengineering.portfolio.CarOwnerProjectApp;
import de.dhbw.softwareengineering.portfolio.domain.Car;
import de.dhbw.softwareengineering.portfolio.repository.CarRepository;
import de.dhbw.softwareengineering.portfolio.service.CarService;
import de.dhbw.softwareengineering.portfolio.repository.search.CarSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CarResource REST controller.
 *
 * @see CarResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CarOwnerProjectApp.class)
@WebAppConfiguration
@IntegrationTest
public class CarResourceIntTest {

    private static final String DEFAULT_MODEL = "AAAAA";
    private static final String UPDATED_MODEL = "BBBBB";
    private static final String DEFAULT_COMPANY = "AAAAA";
    private static final String UPDATED_COMPANY = "BBBBB";

    private static final LocalDate DEFAULT_DATE_OF_PRODUCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_PRODUCTION = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CarRepository carRepository;

    @Inject
    private CarService carService;

    @Inject
    private CarSearchRepository carSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCarMockMvc;

    private Car car;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarResource carResource = new CarResource();
        ReflectionTestUtils.setField(carResource, "carService", carService);
        this.restCarMockMvc = MockMvcBuilders.standaloneSetup(carResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        carSearchRepository.deleteAll();
        car = new Car();
        car.setModel(DEFAULT_MODEL);
        car.setCompany(DEFAULT_COMPANY);
        car.setDateOfProduction(DEFAULT_DATE_OF_PRODUCTION);
    }

    @Test
    @Transactional
    public void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car

        restCarMockMvc.perform(post("/api/cars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = cars.get(cars.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCar.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCar.getDateOfProduction()).isEqualTo(DEFAULT_DATE_OF_PRODUCTION);

        // Validate the Car in ElasticSearch
        Car carEs = carSearchRepository.findOne(testCar.getId());
        assertThat(carEs).isEqualToComparingFieldByField(testCar);
    }

    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the cars
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
                .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
                .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())))
                .andExpect(jsonPath("$.[*].dateOfProduction").value(hasItem(DEFAULT_DATE_OF_PRODUCTION.toString())));
    }

    @Test
    @Transactional
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()))
            .andExpect(jsonPath("$.dateOfProduction").value(DEFAULT_DATE_OF_PRODUCTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar() throws Exception {
        // Initialize the database
        carService.save(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = new Car();
        updatedCar.setId(car.getId());
        updatedCar.setModel(UPDATED_MODEL);
        updatedCar.setCompany(UPDATED_COMPANY);
        updatedCar.setDateOfProduction(UPDATED_DATE_OF_PRODUCTION);

        restCarMockMvc.perform(put("/api/cars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCar)))
                .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(databaseSizeBeforeUpdate);
        Car testCar = cars.get(cars.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCar.getDateOfProduction()).isEqualTo(UPDATED_DATE_OF_PRODUCTION);

        // Validate the Car in ElasticSearch
        Car carEs = carSearchRepository.findOne(testCar.getId());
        assertThat(carEs).isEqualToComparingFieldByField(testCar);
    }

    @Test
    @Transactional
    public void deleteCar() throws Exception {
        // Initialize the database
        carService.save(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Get the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean carExistsInEs = carSearchRepository.exists(car.getId());
        assertThat(carExistsInEs).isFalse();

        // Validate the database is empty
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCar() throws Exception {
        // Initialize the database
        carService.save(car);

        // Search the car
        restCarMockMvc.perform(get("/api/_search/cars?query=id:" + car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())))
            .andExpect(jsonPath("$.[*].dateOfProduction").value(hasItem(DEFAULT_DATE_OF_PRODUCTION.toString())));
    }
}
