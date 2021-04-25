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
@Table(name = "EMPLOYEE")
@DiscriminatorValue("EMPLOYEE")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByOfficeNumber", query = "SELECT e FROM Employee e WHERE e.officeNumber = :officeNumber"),
    })

public class Employee extends Account implements Serializable {

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "OFFICE_NUMBER", nullable=false)
    private String officeNumber; 
    
    @NotNull
    @JoinColumn(name = "CREATED_BY_ADMIN_ID", nullable=false)
    @ManyToOne
    private Administrator createdByAdminId;

    public Employee() {
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public Administrator getCreatedByAdminId() {
        return createdByAdminId;
    }

    public void setCreatedByAdminId(Administrator createdByAdminId) {
        this.createdByAdminId = createdByAdminId;
    }
   
}
