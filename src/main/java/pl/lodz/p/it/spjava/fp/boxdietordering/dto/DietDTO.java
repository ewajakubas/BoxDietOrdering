
package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class DietDTO implements Comparable<DietDTO>{
  
    private Long id;
    
    @NotNull(message="{constraint.notnull}")
    @Size(min = 3, max = 32, message = "{constraint.string.length.notinrange}")
    private String name;
    
    @NotNull(message="{constraint.notnull}")
    @Digits(integer=6, fraction=2)
    @DecimalMin(value="0.01")
    private BigDecimal price;

    @NotNull(message="{constraint.notnull}")   
    private DietCategoryDTO dietCategory;

    public DietCategoryDTO getDietCategory() {
        return dietCategory;
    }

    public void setDietCategory(DietCategoryDTO dietCategory) {
        this.dietCategory = dietCategory;
    }

    public DietDTO(Long id, String name, BigDecimal price, DietCategoryDTO dietCategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dietCategory = dietCategory;
    }
    
    public DietDTO() {
    }
   
    public Long getId() {
        return id;
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

    @Override
    public int compareTo(DietDTO o) {
        return this.dietCategory.getName().compareTo(o.dietCategory.getName());
    }
   
}
