package com.municipality.katilimcivatandas.service;

import com.municipality.katilimcivatandas.domain.Unit;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Unit}.
 */
public interface UnitService {

    /**
     * Save a unit.
     *
     * @param unit the entity to save.
     * @return the persisted entity.
     */
    Unit save(Unit unit);

    /**
     * Get all the units.
     *
     * @return the list of entities.
     */
    List<Unit> findAll();


    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Unit> findOne(Long id);

    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
