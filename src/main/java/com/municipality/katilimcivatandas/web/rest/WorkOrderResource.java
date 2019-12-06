package com.municipality.katilimcivatandas.web.rest;

import com.municipality.katilimcivatandas.domain.WorkOrder;
import com.municipality.katilimcivatandas.service.WorkOrderService;
import com.municipality.katilimcivatandas.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.municipality.katilimcivatandas.domain.WorkOrder}.
 */
@RestController
@RequestMapping("/api")
public class WorkOrderResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderResource.class);

    private static final String ENTITY_NAME = "workOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrderService workOrderService;

    public WorkOrderResource(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    /**
     * {@code POST  /work-orders} : Create a new workOrder.
     *
     * @param workOrder the workOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrder, or with status {@code 400 (Bad Request)} if the workOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-orders")
    public ResponseEntity<WorkOrder> createWorkOrder(@RequestBody WorkOrder workOrder) throws URISyntaxException {
        log.debug("REST request to save WorkOrder : {}", workOrder);
        if (workOrder.getId() != null) {
            throw new BadRequestAlertException("A new workOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkOrder result = workOrderService.save(workOrder);
        return ResponseEntity.created(new URI("/api/work-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-orders} : Updates an existing workOrder.
     *
     * @param workOrder the workOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrder,
     * or with status {@code 400 (Bad Request)} if the workOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-orders")
    public ResponseEntity<WorkOrder> updateWorkOrder(@RequestBody WorkOrder workOrder) throws URISyntaxException {
        log.debug("REST request to update WorkOrder : {}", workOrder);
        if (workOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkOrder result = workOrderService.save(workOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /work-orders} : get all the workOrders.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrders in body.
     */
    @GetMapping("/work-orders")
    public List<WorkOrder> getAllWorkOrders(@RequestParam(required = false) String filter) {
        if ("notification-is-null".equals(filter)) {
            log.debug("REST request to get all WorkOrders where notification is null");
            return workOrderService.findAllWhereNotificationIsNull();
        }
        log.debug("REST request to get all WorkOrders");
        return workOrderService.findAll();
    }

    /**
     * {@code GET  /work-orders/:id} : get the "id" workOrder.
     *
     * @param id the id of the workOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-orders/{id}")
    public ResponseEntity<WorkOrder> getWorkOrder(@PathVariable Long id) {
        log.debug("REST request to get WorkOrder : {}", id);
        Optional<WorkOrder> workOrder = workOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrder);
    }

    /**
     * {@code DELETE  /work-orders/:id} : delete the "id" workOrder.
     *
     * @param id the id of the workOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-orders/{id}")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrder : {}", id);
        workOrderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
