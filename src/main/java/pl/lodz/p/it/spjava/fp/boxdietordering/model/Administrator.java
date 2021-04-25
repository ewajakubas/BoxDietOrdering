package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "ADMINISTRATOR")
@DiscriminatorValue("ADMIN")
@NamedQueries({
    @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
    @NamedQuery(name = "Administrator.findById", query = "SELECT a FROM Administrator a WHERE a.id = :id"),
    @NamedQuery(name = "Administrator.findByAlarmNumber", query = "SELECT a FROM Administrator a WHERE a.alarmNumber = :alarmNumber"),})
public class Administrator extends Account implements Serializable {

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ALARM_NUMBER", nullable=false)
    private String alarmNumber;
    
    @NotNull
    @JoinColumn(name = "CREATED_BY_ADMIN_ID", nullable=false)
    @ManyToOne(optional = false)
    private Administrator createdByAdminId;

    public Administrator() {
    }
    public String getAlarmNumber() {
        return alarmNumber;
    }
    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public Administrator getCreatedByAdminId() {
        return createdByAdminId;
    }

    public void setCreatedByAdminId(Administrator createdByAdminId) {
        this.createdByAdminId = createdByAdminId;
    }
    
}
