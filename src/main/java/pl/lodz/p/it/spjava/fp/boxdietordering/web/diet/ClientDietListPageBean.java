package pl.lodz.p.it.spjava.fp.boxdietordering.web.diet;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.DietEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder.ClientOrderController;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;



@Named("clientDietListPageBean")
@ViewScoped
public class ClientDietListPageBean  implements Serializable{

     @EJB
    private DietEndpoint dietEndpoint;
     
    @Inject
    private ClientOrderController clientOrderController;
    
    private List<DietDTO> listDiets;
    
    private DataModel<DietDTO> dataModelDiets;
    
    public ClientDietListPageBean() {
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

}
