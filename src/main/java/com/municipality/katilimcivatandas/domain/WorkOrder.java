package com.municipality.katilimcivatandas.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.municipality.katilimcivatandas.domain.enumeration.UnitType;

/**
 * A WorkOrder.
 */
@Entity
@Table(name = "work_order")
public class WorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "same_county")
    private Boolean sameCounty;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type")
    private UnitType unitType;

    @OneToOne(mappedBy = "workOrder")
    @JsonIgnore
    private Notification notification;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public WorkOrder createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Boolean isActive() {
        return active;
    }

    public WorkOrder active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isSameCounty() {
        return sameCounty;
    }

    public WorkOrder sameCounty(Boolean sameCounty) {
        this.sameCounty = sameCounty;
        return this;
    }

    public void setSameCounty(Boolean sameCounty) {
        this.sameCounty = sameCounty;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public WorkOrder unitType(UnitType unitType) {
        this.unitType = unitType;
        return this;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Notification getNotification() {
        return notification;
    }

    public WorkOrder notification(Notification notification) {
        this.notification = notification;
        return this;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkOrder)) {
            return false;
        }
        return id != null && id.equals(((WorkOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", active='" + isActive() + "'" +
            ", sameCounty='" + isSameCounty() + "'" +
            ", unitType='" + getUnitType() + "'" +
            "}";
    }
}
