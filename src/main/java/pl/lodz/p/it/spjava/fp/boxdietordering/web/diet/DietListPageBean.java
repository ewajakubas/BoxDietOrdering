package pl.lodz.p.it.spjava.fp.boxdietordering.web.diet;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.DietEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;


@ViewScoped
@Named(value = "dietListPageBean")
public class DietListPageBean implements Serializable {

    @EJB
    private DietEndpoint dietEndpoint;

    @Inject
    private DietController dietControllerPageBean;

    private List<DietDTO> listDiets;

    private DataModel<DietDTO> dataModelDiets;

    public DietListPageBean() {
    }

    public DataModel<DietDTO> getDataModelDiets() {
        return dataModelDiets;
    }

    @PostConstruct
    public void initListDiets() {
        try {
            listDiets = dietEndpoint.listDiets();
            dataModelDiets = new ListDataModel<>(listDiets);
        } catch (AppBaseException ex) {
            Logger.getLogger(DietListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
    }

    public String editDietAction(DietDTO dietDTO) {
        try {
            dietControllerPageBean.selectDietForChange(dietDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(DietListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListDiets();
        return "editDiet";
    }

    public String deleteSelectedDietAction(DietDTO dietDTO) {
        try {
            dietControllerPageBean.selectDietForChange(dietDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(DietListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListDiets();
        return "deleteDiet";
    }
}
