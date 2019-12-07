package com.municipality.katilimcivatandas.web.rest;

import com.municipality.katilimcivatandas.KatilimcivatandasApp;
import com.municipality.katilimcivatandas.domain.WorkOrder;
import com.municipality.katilimcivatandas.repository.WorkOrderRepository;
import com.municipality.katilimcivatandas.service.WorkOrderService;
import com.municipality.katilimcivatandas.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.municipality.katilimcivatandas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.municipality.katilimcivatandas.domain.enumeration.UnitType;
/**
 * Integration tests for the {@link WorkOrderResource} REST controller.
 */
@SpringBootTest(classes = KatilimcivatandasApp.class)
public class WorkOrderResourceIT {

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINISH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_SAME_COUNTY = false;
    private static final Boolean UPDATED_SAME_COUNTY = true;

    private static final UnitType DEFAULT_UNIT_TYPE = UnitType.TECHNICAL;
    private static final UnitType UPDATED_UNIT_TYPE = UnitType.OPERATION;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWorkOrderMockMvc;

    private WorkOrder workOrder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkOrderResource workOrderResource = new WorkOrderResource(workOrderService);
        this.restWorkOrderMockMvc = MockMvcBuilders.standaloneSetup(workOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrder createEntity(EntityManager em) {
        WorkOrder workOrder = new WorkOrder()
            .createDate(DEFAULT_CREATE_DATE)
            .finishDate(DEFAULT_FINISH_DATE)
            .active(DEFAULT_ACTIVE)
            .sameCounty(DEFAULT_SAME_COUNTY)
            .unitType(DEFAULT_UNIT_TYPE);
        return workOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrder createUpdatedEntity(EntityManager em) {
        WorkOrder workOrder = new WorkOrder()
            .createDate(UPDATED_CREATE_DATE)
            .finishDate(UPDATED_FINISH_DATE)
            .active(UPDATED_ACTIVE)
            .sameCounty(UPDATED_SAME_COUNTY)
            .unitType(UPDATED_UNIT_TYPE);
        return workOrder;
    }

    @BeforeEach
    public void initTest() {
        workOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkOrder() throws Exception {
        int databaseSizeBeforeCreate = workOrderRepository.findAll().size();

        // Create the WorkOrder
        restWorkOrderMockMvc.perform(post("/api/work-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workOrder)))
            .andExpect(status().isCreated());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrder testWorkOrder = workOrderList.get(workOrderList.size() - 1);
        assertThat(testWorkOrder.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWorkOrder.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);
        assertThat(testWorkOrder.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testWorkOrder.isSameCounty()).isEqualTo(DEFAULT_SAME_COUNTY);
        assertThat(testWorkOrder.getUnitType()).isEqualTo(DEFAULT_UNIT_TYPE);
    }

    @Test
    @Transactional
    public void createWorkOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workOrderRepository.findAll().size();

        // Create the WorkOrder with an existing ID
        workOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkOrderMockMvc.perform(post("/api/work-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workOrder)))
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWorkOrders() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        // Get all the workOrderList
        restWorkOrderMockMvc.perform(get("/api/work-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].sameCounty").value(hasItem(DEFAULT_SAME_COUNTY.booleanValue())))
            .andExpect(jsonPath("$.[*].unitType").value(hasItem(DEFAULT_UNIT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getWorkOrder() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        // Get the workOrder
        restWorkOrderMockMvc.perform(get("/api/work-orders/{id}", workOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workOrder.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.sameCounty").value(DEFAULT_SAME_COUNTY.booleanValue()))
            .andExpect(jsonPath("$.unitType").value(DEFAULT_UNIT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkOrder() throws Exception {
        // Get the workOrder
        restWorkOrderMockMvc.perform(get("/api/work-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkOrder() throws Exception {
        // Initialize the database
        workOrderService.save(workOrder);

        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();

        // Update the workOrder
        WorkOrder updatedWorkOrder = workOrderRepository.findById(workOrder.getId()).get();
        // Disconnect from session so that the updates on updatedWorkOrder are not directly saved in db
        em.detach(updatedWorkOrder);
        updatedWorkOrder
            .createDate(UPDATED_CREATE_DATE)
            .finishDate(UPDATED_FINISH_DATE)
            .active(UPDATED_ACTIVE)
            .sameCounty(UPDATED_SAME_COUNTY)
            .unitType(UPDATED_UNIT_TYPE);

        restWorkOrderMockMvc.perform(put("/api/work-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkOrder)))
            .andExpect(status().isOk());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
        WorkOrder testWorkOrder = workOrderList.get(workOrderList.size() - 1);
        assertThat(testWorkOrder.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWorkOrder.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);
        assertThat(testWorkOrder.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testWorkOrder.isSameCounty()).isEqualTo(UPDATED_SAME_COUNTY);
        assertThat(testWorkOrder.getUnitType()).isEqualTo(UPDATED_UNIT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkOrder() throws Exception {
        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();

        // Create the WorkOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrderMockMvc.perform(put("/api/work-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workOrder)))
            .andExpect(status().isBadRequest());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkOrder() throws Exception {
        // Initialize the database
        workOrderService.save(workOrder);

        int databaseSizeBeforeDelete = workOrderRepository.findAll().size();

        // Delete the workOrder
        restWorkOrderMockMvc.perform(delete("/api/work-orders/{id}", workOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkOrder> workOrderList = workOrderRepository.findAll();
        assertThat(workOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
