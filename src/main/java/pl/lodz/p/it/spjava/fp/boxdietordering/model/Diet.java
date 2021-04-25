package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@TableGenerator(name = "DietIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "Diet", initialValue = 100)
@Entity
@Table(name = "DIET")
@NamedQueries({
    @NamedQuery(name = "Diet.findAll", query = "SELECT d FROM Diet d"),
    @NamedQuery(name = "Diet.findById", query = "SELECT d FROM Diet d WHERE d.id = :id"),
    @NamedQuery(name = "Diet.findByName", query = "SELECT d FROM Diet d WHERE d.name = :name"),
    @NamedQuery(name = "Diet.findByPrice", query = "SELECT d FROM Diet d WHERE d.price = :price"),
    })

public class Diet extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        return name;
    }
    
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DietIdGen")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }
    
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME",nullable = false, /*unique = true, ograniczenie w initDB*/updatable= true)
    private String name;  
    
    @Digits(integer=6, fraction=2)
    @DecimalMin(value="0.01")
    @Column(name = "PRICE", precision = 8, scale = 2, nullable = false, updatable= true)
    private BigDecimal price;

    @NotNull
    @JoinColumn(name = "CREATED_BY_EMPLOYEE_ID", nullable=false)
    @ManyToOne
    private Employee createdByEmployeeId;
    
    @JoinColumn(name = "MODIFIED_BY_EMPLOYEE_ID")
    @ManyToOne
    private Employee modifiedByEmployeeId;
    
    @NotNull
    @JoinColumn(name = "DietCategory_id")
    @ManyToOne
    private DietCategory dietCategory;

    public DietCategory getDietCategory() {
        return dietCategory;
    }

    public void setDietCategory(DietCategory dietCategory) {
        this.dietCategory = dietCategory;
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Employee getCreatedByEmployeeId() {
        return createdByEmployeeId;
    }

    public void setCreatedByEmployeeId(Employee createdByEmployeeId) {
        this.createdByEmployeeId = createdByEmployeeId;
    }

    public Employee getModifiedByEmployeeId() {
        return modifiedByEmployeeId;
    }

    public void setModifiedByEmployeeId(Employee modifiedByEmployeeId) {
        this.modifiedByEmployeeId = modifiedByEmployeeId;
    }

  
    }


 


    

