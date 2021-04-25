
package pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.OrderItemDTO;

@Named("detailsMyClientOrderPageBean")
@RequestScoped
public class DetailsMyClientOrderPageBean {

    public DetailsMyClientOrderPageBean() {
    }
    
    @Inject
    private ClientOrderController clientOrderController;
       
    private OrderItemDTO orderItem;

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }
    
    public ClientOrderDTO getClientOrder() {
        return clientOrderController.getClientOrderView();
    }

  
    
}
