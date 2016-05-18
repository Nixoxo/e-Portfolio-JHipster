package de.dhbw.softwareengineering.portfolio.repository.search;

import de.dhbw.softwareengineering.portfolio.domain.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Car entity.
 */
public interface CarSearchRepository extends ElasticsearchRepository<Car, Long> {
}
