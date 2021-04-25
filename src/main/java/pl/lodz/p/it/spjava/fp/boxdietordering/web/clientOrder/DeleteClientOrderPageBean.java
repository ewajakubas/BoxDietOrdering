package pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;


@Named("deleteClientOrderPageBean")
@RequestScoped
public class DeleteClientOrderPageBean {
    
    public DeleteClientOrderPageBean() {
    }
    
    @Inject
    private ClientOrderController clientOrderController;

    public ClientOrderDTO getClientOrder() {
        return clientOrderController.getClientOrderEdit();
    }

    public String deleteClientOrder() {
        return clientOrderController.deleteSelectedClientOrder();
    }

}
