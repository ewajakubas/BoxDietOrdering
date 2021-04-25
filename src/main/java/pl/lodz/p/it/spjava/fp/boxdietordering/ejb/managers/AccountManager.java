package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.managers;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.AccountFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Administrator;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Client;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class AccountManager extends AbstractManager
        implements SessionSynchronization {

    @Inject
    private AccountFacade accountFacade;

    @PermitAll
    public void createAccount(Client client) throws AppBaseException {
        accountFacade.create(client);
    }

    @RolesAllowed({"Administrator"})
    public void createAccount(Employee employee) throws AppBaseException {
        accountFacade.create(employee);
    }

    @RolesAllowed({"Administrator"})
    public void createAccount(Administrator administrator) throws AppBaseException {
        accountFacade.create(administrator);
    }

    @RolesAllowed({"Administrator"})
    public void edit(Account accountState) throws AppBaseException {
        accountFacade.edit(accountState);
    }

    @RolesAllowed({"Administrator"})
    public void active(Account account) throws AppBaseException {
        accountFacade.edit(account);
    }

    @RolesAllowed({"Administrator"})
    public void deactive(Account account) throws AppBaseException {
        accountFacade.edit(account);
    }
}
