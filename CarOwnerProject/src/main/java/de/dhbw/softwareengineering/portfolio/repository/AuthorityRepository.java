package de.dhbw.softwareengineering.portfolio.repository;

import de.dhbw.softwareengineering.portfolio.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
