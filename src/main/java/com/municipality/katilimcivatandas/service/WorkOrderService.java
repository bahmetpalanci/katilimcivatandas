package com.municipality.katilimcivatandas.service;

import com.municipality.katilimcivatandas.domain.Notification;
import com.municipality.katilimcivatandas.domain.WorkOrder;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkOrder}.
 */
public interface WorkOrderService {

    /**
     * Save a workOrder.
     *
     * @param workOrder the entity to save.
     * @return the persisted entity.
     */
    WorkOrder save(WorkOrder workOrder);

    /**
     * Get all the workOrders.
     *
     * @return the list of entities.
     */
    List<WorkOrder> findAll();
    /**
     * Get all the WorkOrderDTO where Notification is {@code null}.
     *
     * @return the list of entities.
     */
    List<WorkOrder> findAllWhereNotificationIsNull();


    /**
     * Get the "id" workOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkOrder> findOne(Long id);

    /**
     * Delete the "id" workOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void doWork();

    void createWorkOrderFromNotification(Notification notification);
}
