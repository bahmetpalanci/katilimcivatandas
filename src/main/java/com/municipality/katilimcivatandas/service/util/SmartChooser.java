package com.municipality.katilimcivatandas.service.util;

import com.municipality.katilimcivatandas.domain.enumeration.NotificationCategory;
import com.municipality.katilimcivatandas.domain.enumeration.UnitType;
import org.springframework.stereotype.Component;


@Component
public class SmartChooser {
    public UnitType chooseCorrectUnitType(NotificationCategory notificationCategory) {
        if (notificationCategory == NotificationCategory.BREAKDOWN) {
            return UnitType.TECHNICAL;
        } else if (notificationCategory == NotificationCategory.REQUEST) {
            return UnitType.OPERATION;
        } else {
            return UnitType.SUPPORT;
        }
    }
}
