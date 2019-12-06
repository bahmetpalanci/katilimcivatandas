package com.municipality.katilimcivatandas.service.impl;

import com.municipality.katilimcivatandas.domain.Notification;
import com.municipality.katilimcivatandas.service.WorkOrderService;
import com.municipality.katilimcivatandas.domain.WorkOrder;
import com.municipality.katilimcivatandas.repository.WorkOrderRepository;
import com.municipality.katilimcivatandas.service.util.SmartChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link WorkOrder}.
 */
@Service
@Transactional
public class WorkOrderServiceImpl implements WorkOrderService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

    private final WorkOrderRepository workOrderRepository;
    private final SmartChooser smartChooser;

    public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository, SmartChooser smartChooser) {
        this.workOrderRepository = workOrderRepository;
        this.smartChooser = smartChooser;
    }

    /**
     * Save a workOrder.
     *
     * @param workOrder the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WorkOrder save(WorkOrder workOrder) {
        log.debug("Request to save WorkOrder : {}", workOrder);
        return workOrderRepository.save(workOrder);
    }

    /**
     * Get all the workOrders.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> findAll() {
        log.debug("Request to get all WorkOrders");
        return workOrderRepository.findAll();
    }


    /**
     * Get all the workOrders where Notification is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkOrder> findAllWhereNotificationIsNull() {
        log.debug("Request to get all workOrders where Notification is null");
        return StreamSupport
            .stream(workOrderRepository.findAll().spliterator(), false)
            .filter(workOrder -> workOrder.getNotification() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one workOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkOrder> findOne(Long id) {
        log.debug("Request to get WorkOrder : {}", id);
        return workOrderRepository.findById(id);
    }

    /**
     * Delete the workOrder by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkOrder : {}", id);
        workOrderRepository.deleteById(id);
    }

    @Override
    public void doWork() {
        log.debug("Request to doWork WorkOrder");
        final List<WorkOrder> activeWorkOrders = workOrderRepository.findByActive(true);
        for (WorkOrder workOrder : activeWorkOrders) {
            if (workOrder.getNotification() != null) {
                workOrder.setActive(false);
                workOrder.getNotification().setDone(true);
                log.debug("{} user's notification is done", workOrder.getNotification()
                    .getUser()
                    .getUser()
                    .getLogin());
            }
        }
    }

    @Override
    public void createWorkOrderFromNotification(Notification notification) {
        if (notification == null) {
            return;
        }

        final WorkOrder workOrder = new WorkOrder();
        workOrder.setActive(true);
        workOrder.setSameCounty(notification.isSameCounty());
        workOrder.setCreateDate(LocalDate.now());
        workOrder.setNotification(notification);
        workOrder.setUnitType(smartChooser.chooseCorrectUnitType(notification.getCategory()));
        save(workOrder);
    }
}
