package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.managers;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.ClientOrderFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.ClientOrderException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Client;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.ClientOrder;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class ClientOrderManager extends AbstractManager
        implements SessionSynchronization {

    @Inject
    private AccountEndpoint accountEndpoint;

    @Inject
    private ClientOrderFacade clientOrderFacade;

    @RolesAllowed({"Employee"})
    public void confirmClientOrder(Long clientOrderId) throws AppBaseException {
        ClientOrder clientOrder = clientOrderFacade.find(clientOrderId);
        if (null == clientOrder) {
            throw ClientOrderException.createClientOrderExceptionWithClientOrderNotFound();
        }
        if (clientOrder.isAccepted()) {
            throw ClientOrderException.createClientOrderExceptionWithAproveOfAproved(clientOrder);
        }
        Employee myEmployeeAccount = accountEndpoint.downloadMyAccountEmployee();
        clientOrder.setWhoAccepted(myEmployeeAccount);
        clientOrder.setAccepted(true);
        clientOrderFacade.edit(clientOrder);
    }

    @RolesAllowed({"Client", "Employee"})
    public void deleteClientOrder(Long clientOrderId) throws AppBaseException {
        ClientOrder clientOrder = clientOrderFacade.find(clientOrderId);
        if (null == clientOrder) {
            throw ClientOrderException.createClientOrderExceptionWithClientOrderNotFound();
        }
        if (clientOrder.isAccepted()) {
            throw ClientOrderException.createClientOrderExceptionWithAproveOfAproved(clientOrder);
        } else {
            clientOrderFacade.remove(clientOrder);
        }
    }

    @RolesAllowed({"Client"})
    public void createNewClientOrder(ClientOrder order) throws AppBaseException {
        Client myAccountClient = accountEndpoint.downloadMyAccountClient();
        order.setWhoOrdered(myAccountClient);
        myAccountClient.getClientOrderList().add(order);
        clientOrderFacade.create(order);
    }
}
