package de.dhbw.softwareengineering.portfolio.repository;

import de.dhbw.softwareengineering.portfolio.domain.Owner;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Owner entity.
 */
public interface OwnerRepository extends JpaRepository<Owner,Long> {

}
