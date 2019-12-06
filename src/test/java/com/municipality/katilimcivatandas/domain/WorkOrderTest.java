package com.municipality.katilimcivatandas.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.municipality.katilimcivatandas.web.rest.TestUtil;

public class WorkOrderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrder.class);
        WorkOrder workOrder1 = new WorkOrder();
        workOrder1.setId(1L);
        WorkOrder workOrder2 = new WorkOrder();
        workOrder2.setId(workOrder1.getId());
        assertThat(workOrder1).isEqualTo(workOrder2);
        workOrder2.setId(2L);
        assertThat(workOrder1).isNotEqualTo(workOrder2);
        workOrder1.setId(null);
        assertThat(workOrder1).isNotEqualTo(workOrder2);
    }
}
