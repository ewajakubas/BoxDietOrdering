package pl.lodz.p.it.spjava.fp.boxdietordering.web.diet;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;


@Named(value = "editDietPageBean")
@RequestScoped
public class EditDietPageBean {
    
    @Inject
    private DietController dietController;
    
    private DietDTO dietDTO;

    public EditDietPageBean() {
    }

    public DietDTO getDietDTO() {
        return dietDTO;
    }

    public void setDietDTO(DietDTO dietDTO) {
        this.dietDTO = dietDTO;
    }

    @PostConstruct
    public void init() {
        dietDTO = dietController.getSelectedDietDTO();
    }
    private DietDTO diet = new DietDTO();

    public DietDTO getDiet() {
        return diet;
    }

    public void setDiet(DietDTO diet) {
        this.diet = diet;
    }
    
    public String saveEditDietAction() throws AppBaseException {
        return dietController.saveEditedDiet(dietDTO);
    }

}
