package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.OrderItemDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.ClientOrderFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.DietFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.OrderItemFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.managers.ClientOrderManager;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.ClientOrderException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.ClientOrder;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.OrderItem;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.DTOConverter;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.NEVER)
@Interceptors(LoggingInterceptor.class)
public class ClientOrderEndpoint {

    @Inject
    private AccountEndpoint accountEndpoint;

    @Inject
    private ClientOrderManager clientOrderManager;

    @Inject
    private ClientOrderFacade clientOrderFacade;

    @Inject
    private OrderItemFacade orderItemFacade;

    @Inject
    private DietFacade dietFacade;

    @Resource(name = "txRetryLimit")
    private int txRetryLimit;

    @RolesAllowed({"Client", "Employee"})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ClientOrderDTO downloadOrder(ClientOrderDTO clientOrderDTO) throws AppBaseException {
        ClientOrder clientOrder = clientOrderFacade.find(clientOrderDTO.getId());
        if (null == clientOrder) {
            throw ClientOrderException.createClientOrderExceptionWithClientOrderNotFound();
        }
        return DTOConverter.createClientOrderDTOFromEntity(clientOrder);
    }

    @RolesAllowed({"Employee"})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ClientOrderDTO> downloadAllClientOrders() {
        return DTOConverter.createClientOrderListDTOfromEntity(clientOrderFacade.findAll());
    }

    @RolesAllowed({"Employee"})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ClientOrderDTO> downloadClientOrdersNotAccepted() {
        return DTOConverter.createClientOrderListDTOfromEntity(clientOrderFacade.findNotAcceptedClientOrders());
    }
    
    @RolesAllowed({"Employee"})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ClientOrderDTO> downloadClientOrdersAccepted() {
        return DTOConverter.createClientOrderListDTOfromEntity(clientOrderFacade.findAcceptedClientOrders());
    }

    @RolesAllowed({"Client"})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ClientOrderDTO> downloadMyClientOrders() {
        return DTOConverter.createClientOrderListDTOfromEntity(clientOrderFacade.findClientOrdersForClient(accountEndpoint.downloadMyLogin()));
    }

    @RolesAllowed({"Client"})
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<ClientOrderDTO> downloadMyClientOrdersNotAccepted() {
        return DTOConverter.createClientOrderListDTOfromEntity(clientOrderFacade.findClientOrdersNotAcceptedForClient(accountEndpoint.downloadMyLogin())); // Wersja produkcyjna przy zastosowaniu poprawnego uwierzytelniania
    }
    @RolesAllowed({"Client"})
    public void createNewClientOrder(ClientOrderDTO clientOrderDTO) throws AppBaseException {
        ClientOrder order = new ClientOrder();
        for (OrderItemDTO orderItemDTO : clientOrderDTO.getOrderItemList()) {
            List<Diet> dietList = dietFacade.findAll();
            Diet selectedDiet = null;
            for (Diet diet : dietList) {
                if (diet.getName().equals(orderItemDTO.getDiet().getName())) {
                    selectedDiet = diet;
                    break;
                }
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setDiet(selectedDiet);
            orderItem.setDaysNb(orderItemDTO.getDaysNb());
            orderItem.setDateFrom(orderItemDTO.getDateFrom());
            orderItem.setPrice(selectedDiet.getPrice());

            order.getOrderItemList().add(orderItem);
        }
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientOrderManager.createNewClientOrder(order);
                rollbackTX = clientOrderManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej createNewClientOrder zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw ClientOrderException.createClientOrderExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Employee"})
    public void confirmClientOrder(ClientOrderDTO clientOrderDTO) throws AppBaseException {
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientOrderManager.confirmClientOrder(clientOrderDTO.getId());
                rollbackTX = clientOrderManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej confirmClientOrder zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw ClientOrderException.createClientOrderExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Client", "Employee"})
    public void deleteClientOrder(ClientOrderDTO clientOrderDTO) throws AppBaseException {
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                clientOrderManager.deleteClientOrder(clientOrderDTO.getId());
                rollbackTX = clientOrderManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej deleteClientOrder zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw ClientOrderException.createClientOrderExceptionWithTxRetryRollback();
        }

    }

}
