package com.municipality.katilimcivatandas.schedule;

import com.municipality.katilimcivatandas.repository.WorkOrderRepository;
import com.municipality.katilimcivatandas.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class WorkOrderScheduler {

    @Autowired
    private WorkOrderService workOrderService;

    @Scheduled(fixedRate = 1000 * 60)
    public void scheduleFixedRateTask() {
        System.out.println("Work Order Job started at " + LocalDateTime.now());
        workOrderService.doWork();
        System.out.println("Work Order Job ended at " + LocalDateTime.now());
    }
}
