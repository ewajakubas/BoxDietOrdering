package pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.ClientOrderEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.ClientOrderException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.account.AccountController;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;

@SessionScoped
public class ClientOrderController implements Serializable {

    @Inject
    private ClientOrderEndpoint clientOrderEndpoint;

    private ClientOrderDTO clientOrderEdit;
    private ClientOrderDTO ordering;
    private ClientOrderDTO clientOrderView;
    private ClientOrderDTO clientOrderCreate;

    public void resetOrdering() {
        ordering = new ClientOrderDTO();
    }

    public ClientOrderDTO getOrdering() {
        return ordering;
    }

    public ClientOrderDTO getClientOrderEdit() {
        return clientOrderEdit;
    }

    public ClientOrderDTO getClientOrderView() {
        return clientOrderView;
    }

    public String downloadClientOrderToConfirm(ClientOrderDTO clientOrder) {
        try {
            this.clientOrderEdit = clientOrderEndpoint.downloadOrder(clientOrder);
            return "confirmClientOrder";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ClientOrderController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji downloadClientOrderToConfirm wyjatku: ", ex);

            ContextUtils.emitI18NdMessageOfException(ex);
            return null;
        }
    }

    public String downloadClientOrderToViewEmployee(ClientOrderDTO clientOrder) {
        try {
            this.clientOrderView = clientOrderEndpoint.downloadOrder(clientOrder);
            return "detailsClientOrder";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ClientOrderController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji downloadClientOrderToView wyjatku: ", ex);

            ContextUtils.emitI18NdMessageOfException(ex);
            return null;
        }
    }

    public String downloadClientOrderToViewClient(ClientOrderDTO clientOrder) {
        try {
            this.clientOrderView = clientOrderEndpoint.downloadOrder(clientOrder);
            return "detailsMyClientOrder";
        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ClientOrderController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji downloadClientOrderToView wyjatku: ", ex);

            ContextUtils.emitI18NdMessageOfException(ex);
            return null;
        }
    }

    public String downloadClientOrderToRemoveForEmployee(ClientOrderDTO clientOrder) {
        try {
            this.clientOrderEdit = clientOrderEndpoint.downloadOrder(clientOrder);
            return "deleteClientOrder";

        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ClientOrderController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji downloadClientOrderToRemove wyjatku: ", ex);

            ContextUtils.emitI18NdMessageOfException(ex);
            return null;
        }
    }

    public String downloadClientOrderToRemoveForClient(ClientOrderDTO clientOrder) {
        try {
            this.clientOrderEdit = clientOrderEndpoint.downloadOrder(clientOrder);
            return "deleteClientOrder";

        } catch (AppBaseException ex) {
            Logger lg = Logger.getLogger(ClientOrderController.class.getName());
            lg.log(Level.SEVERE, "Zgłoszenie w metodzie akcji downloadClientOrderToRemove wyjatku: ", ex);

            ContextUtils.emitI18NdMessageOfException(ex);
            return null;
        }
    }

    public String confirmSelectedClientOrder() {
        try {
            clientOrderEndpoint.confirmClientOrder(clientOrderEdit);
            return "success";
        } catch (ClientOrderException oe) {
            if (ClientOrderException.KEY_APROVE_OF_APROVED.equals(oe.getMessage())) {
                ContextUtils.emitI18NMessage(null, ClientOrderException.KEY_APROVE_OF_APROVED);
            } else if (ClientOrderException.KEY_CLIENTORDER_NOT_FOUND.equals(oe.getMessage())) {
                ContextUtils.emitI18NMessage(null, ClientOrderException.KEY_CLIENTORDER_NOT_FOUND);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of confirmSelectedClientOrder using the method: ", oe);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(ClientOrderController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji confirmSelectedClientOrder wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String createNewClientOrder(ClientOrderDTO clientOrderDTO) {
        try {
            clientOrderCreate = clientOrderDTO;
            clientOrderEndpoint.createNewClientOrder(clientOrderCreate);
            clientOrderCreate = null;
            return "messagesForOrder";
        } catch (AppBaseException abe) {
            Logger.getLogger(ClientOrderController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji createNewClientOrder wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String deleteSelectedClientOrder() {
        try {
            clientOrderEndpoint.deleteClientOrder(clientOrderEdit);
            return "success";
        } catch (ClientOrderException oe) {
            if (ClientOrderException.KEY_APROVE_OF_APROVED.equals(oe.getMessage())) {
                ContextUtils.emitI18NMessage(null, ClientOrderException.KEY_APROVE_OF_APROVED);
            } else if (ClientOrderException.KEY_CLIENTORDER_NOT_FOUND.equals(oe.getMessage())) {
                ContextUtils.emitI18NMessage(null, ClientOrderException.KEY_CLIENTORDER_NOT_FOUND);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of deleteSelectedClientOrder using the method: ", oe);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(ClientOrderController.class.getName()).log(Level.SEVERE, "Zgłoszenie w metodzie akcji confirmSelectedClientOrder wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public List<ClientOrderDTO> downloadAllClientOrders() {
        return clientOrderEndpoint.downloadAllClientOrders();
    }

    public List<ClientOrderDTO> downloadClientOrdersNotAccepted() {
        return clientOrderEndpoint.downloadClientOrdersNotAccepted();
    }

    public List<ClientOrderDTO> downloadClientOrdersAccepted() {
        return clientOrderEndpoint.downloadClientOrdersAccepted();
    }

    public List<ClientOrderDTO> downloadMyClientOrders() {
        return clientOrderEndpoint.downloadMyClientOrders();
    }

    public List<ClientOrderDTO> downloadMyClientOrdersNotAccepted() {
        return clientOrderEndpoint.downloadMyClientOrdersNotAccepted();
    }

}
