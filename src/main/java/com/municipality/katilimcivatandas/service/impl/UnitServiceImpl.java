package com.municipality.katilimcivatandas.service.impl;

import com.municipality.katilimcivatandas.service.UnitService;
import com.municipality.katilimcivatandas.domain.Unit;
import com.municipality.katilimcivatandas.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Unit}.
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

    private final UnitRepository unitRepository;

    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * Save a unit.
     *
     * @param unit the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Unit save(Unit unit) {
        log.debug("Request to save Unit : {}", unit);
        return unitRepository.save(unit);
    }

    /**
     * Get all the units.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Unit> findAll() {
        log.debug("Request to get all Units");
        return unitRepository.findAll();
    }


    /**
     * Get one unit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Unit> findOne(Long id) {
        log.debug("Request to get Unit : {}", id);
        return unitRepository.findById(id);
    }

    /**
     * Delete the unit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Unit : {}", id);
        unitRepository.deleteById(id);
    }
}
