package pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.OrderItemDTO;

@Named("clientOrdersListForEmployeePageBean")
@ViewScoped
public class ClientOrdersListForEmployeePageBean implements Serializable {

    public ClientOrdersListForEmployeePageBean() {
    }

    private boolean onlyNotAccepted = false;
    private boolean onlyAccepted = false;
    private List<ClientOrderDTO> clientOrderList;
    private OrderItemDTO orderItem;

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    @PostConstruct
    private void initModel() {
        if (onlyNotAccepted) {
            clientOrderList = clientOrderController.downloadClientOrdersNotAccepted();
        } else if (onlyAccepted) {
            clientOrderList = clientOrderController.downloadClientOrdersAccepted();
        } else {
            clientOrderList = clientOrderController.downloadAllClientOrders();
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

    public boolean isOnlyAccepted() {
        return onlyAccepted;
    }

    public void setOnlyAccepted(boolean onlyAccepted) {
        this.onlyAccepted = onlyAccepted;
    }

    public String confirmSelectedClientOrder(ClientOrderDTO clientOrder) {
        return clientOrderController.downloadClientOrderToConfirm(clientOrder);
    }

    public void refresh() {
        initModel();
    }

    public String viewSelectedClientOrder(ClientOrderDTO clientOrder) {
        return clientOrderController.downloadClientOrderToViewEmployee(clientOrder);
    }
        public String deleteSelectedClientOrder(ClientOrderDTO clientOrder) {
        return clientOrderController.downloadClientOrderToRemoveForEmployee(clientOrder);
    }
}
