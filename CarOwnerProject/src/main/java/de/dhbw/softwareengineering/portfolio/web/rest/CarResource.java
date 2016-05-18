package de.dhbw.softwareengineering.portfolio.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.dhbw.softwareengineering.portfolio.domain.Car;
import de.dhbw.softwareengineering.portfolio.service.CarService;
import de.dhbw.softwareengineering.portfolio.web.rest.util.HeaderUtil;
import de.dhbw.softwareengineering.portfolio.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Car.
 */
@RestController
@RequestMapping("/api")
public class CarResource {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);
        
    @Inject
    private CarService carService;
    
    /**
     * POST  /cars : Create a new car.
     *
     * @param car the car to create
     * @return the ResponseEntity with status 201 (Created) and with body the new car, or with status 400 (Bad Request) if the car has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cars",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car> createCar(@RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to save Car : {}", car);
        if (car.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("car", "idexists", "A new car cannot already have an ID")).body(null);
        }
        Car result = carService.save(car);
        return ResponseEntity.created(new URI("/api/cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("car", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cars : Updates an existing car.
     *
     * @param car the car to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated car,
     * or with status 400 (Bad Request) if the car is not valid,
     * or with status 500 (Internal Server Error) if the car couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car> updateCar(@RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to update Car : {}", car);
        if (car.getId() == null) {
            return createCar(car);
        }
        Car result = carService.save(car);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("car", car.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cars : get all the cars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cars in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Car>> getAllCars(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cars");
        Page<Car> page = carService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cars/:id : get the "id" car.
     *
     * @param id the id of the car to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the car, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cars/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        log.debug("REST request to get Car : {}", id);
        Car car = carService.findOne(id);
        return Optional.ofNullable(car)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cars/:id : delete the "id" car.
     *
     * @param id the id of the car to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cars/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        log.debug("REST request to delete Car : {}", id);
        carService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("car", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cars?query=:query : search for the car corresponding
     * to the query.
     *
     * @param query the query of the car search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Car>> searchCars(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Cars for query {}", query);
        Page<Car> page = carService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
