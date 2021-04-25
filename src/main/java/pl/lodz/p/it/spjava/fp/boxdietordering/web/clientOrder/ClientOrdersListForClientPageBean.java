package pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;


@Named("clientOrdersListForClientPageBean")
@ViewScoped
public class ClientOrdersListForClientPageBean  implements Serializable{

    public ClientOrdersListForClientPageBean() {
    }
  private boolean onlyNotAccepted = false;
    private List<ClientOrderDTO> clientOrderList;


    @PostConstruct
    private void initModel() {
        if (onlyNotAccepted) {
            clientOrderList = clientOrderController.downloadMyClientOrdersNotAccepted();
        } else {
            clientOrderList = clientOrderController.downloadMyClientOrders();
        }
    }
    @Inject
    private ClientOrderController clientOrderController;

    public List<ClientOrderDTO> getClientOrderList() {
        return clientOrderList;
    }

       public boolean isOnlyNotAccepted() {
        return onlyNotAccepted;
    }

   public void setOnlyNotAccepted(boolean onlyNotAccepted) {
        this.onlyNotAccepted = onlyNotAccepted;
    }

    public String deleteSelectedClientOrder(ClientOrderDTO clientOrder) {
        return clientOrderController.downloadClientOrderToRemoveForClient(clientOrder);
    }
     public String viewSelectedClientOrder(ClientOrderDTO clientOrder) {
        return clientOrderController.downloadClientOrderToViewClient(clientOrder);
    }

    public void refresh() {
        initModel();
    }
}
