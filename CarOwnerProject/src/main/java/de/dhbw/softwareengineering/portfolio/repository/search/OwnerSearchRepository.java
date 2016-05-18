package de.dhbw.softwareengineering.portfolio.repository.search;

import de.dhbw.softwareengineering.portfolio.domain.Owner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Owner entity.
 */
public interface OwnerSearchRepository extends ElasticsearchRepository<Owner, Long> {
}
