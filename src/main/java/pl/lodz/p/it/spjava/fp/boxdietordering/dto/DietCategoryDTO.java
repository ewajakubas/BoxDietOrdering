package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class DietCategoryDTO {
    
    private Long id;
    
    @NotNull
    @Size(min=3,max=32,message="{constraint.string.length.notinrange}")
    private String name;

    public DietCategoryDTO() {
    }

    public DietCategoryDTO(String name) {
        this.name = name;
    }
    
    public DietCategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
    
}
