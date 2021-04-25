package pl.lodz.p.it.spjava.fp.boxdietordering.web.diet;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietCategoryDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.DietCategoryEndpoint;


@Named(value = "createNewDietPageBean")
@RequestScoped
public class CreateNewDietPageBean implements Serializable {

    @EJB
    private DietCategoryEndpoint dietCategoryEndpoint;
    @Inject
    private DietController dietController;
    
    private String selectedDietCategoryName;
    
    public String getSelectedDietCategoryName() {
        return selectedDietCategoryName;
    }
    public void setSelectedDietCategoryName(String selectedDietCategoryName) {
        this.selectedDietCategoryName = selectedDietCategoryName;
    }
    private DietDTO diet = new DietDTO();
    
    public DietDTO getDiet() {
        return diet;
    }
    public CreateNewDietPageBean() {
    }

    @PostConstruct
    public void init() {
        dietCategoryList = dietCategoryEndpoint.dietCategoryList();
    }
    private List<DietCategoryDTO> dietCategoryList;

    public List<DietCategoryDTO> getDietCategoryList() {
        return dietCategoryList;
    }
    public void setDietCategoryList(List<DietCategoryDTO> dietCategoryList) {
        this.dietCategoryList = dietCategoryList;
    }
    public String createNewDietAction() {
        for (DietCategoryDTO dietCategoryDTO : dietCategoryList) {
            if (dietCategoryDTO.getName().equals(selectedDietCategoryName)) {
                diet.setDietCategory(dietCategoryDTO);
                break;
            }
        }
        return dietController.createNewDiet(diet);
    }
}
