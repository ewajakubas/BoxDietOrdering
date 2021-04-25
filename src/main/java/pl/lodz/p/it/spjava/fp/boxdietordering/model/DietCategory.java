package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@TableGenerator(name = "CategoryIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "DietCategory", initialValue=100)
@Entity
@Table(name = "DIET_CATEGORY")

@NamedQueries({
    @NamedQuery(name = "DietCategory.findAll", query = "SELECT d FROM DietCategory d"),
    @NamedQuery(name = "DietCategory.findById", query = "SELECT d FROM DietCategory d WHERE d.id = :id"),
    @NamedQuery(name = "DietCategory.findByName", query = "SELECT d FROM DietCategory d WHERE d.name = :name")})

public class DietCategory extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        return name;
    }
    
    @Id
    @Column(name = "id", updatable = false) 
  
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CategoryIdGen")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }
    
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME", length = 32, nullable = false, unique = true, updatable = false)
    private String name;

    public void setId(Long id) {
        this.id = id;
    }
       
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   
}
    
    

   

    

