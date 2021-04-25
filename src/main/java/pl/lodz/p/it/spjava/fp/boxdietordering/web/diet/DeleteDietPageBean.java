package pl.lodz.p.it.spjava.fp.boxdietordering.web.diet;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;


@Named(value = "deleteDietPageBean")
@RequestScoped
public class DeleteDietPageBean {

    @Inject
    private DietController dietController;

    private DietDTO dietDTO;

    public DeleteDietPageBean() {
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

    public String deleteDietAction() throws AppBaseException {
        return dietController.deleteDiet(dietDTO);

    }

}
