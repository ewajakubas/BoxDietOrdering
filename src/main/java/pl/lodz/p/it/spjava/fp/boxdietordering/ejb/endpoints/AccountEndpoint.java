package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AdministratorDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.EmployeeDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.AccountFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.AdministratorFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.ClientFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.EmployeeFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Administrator;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Client;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;
import pl.lodz.p.it.spjava.fp.boxdietordering.security.HashGenerator;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.managers.AccountManager;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AccountException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.DTOConverter;

@Stateful
@LocalBean
@RolesAllowed({"Administrator"})
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private AccountManager accountManager;

    @Inject
    private ClientFacade clientFacade;

    @Inject
    private EmployeeFacade employeeFacade;

    @Inject
    private AdministratorFacade administratorFacade;

    @Inject
    private HashGenerator hashGenerator;

    @Resource
    protected SessionContext sessionContext;

    @Resource(name = "txRetryLimit")
    private int txRetryLimit;

    private Account accountState;

    private Account myAccountState;
    

    @RolesAllowed({"Administrator"})
    public List<AccountDTO> listAllAccount() throws AppBaseException {
        return DTOConverter.createAccountsListDTOFromEntity(accountFacade.findAll());
    }

    @RolesAllowed({"Employee"})
    public List<ClientDTO> listClientsAccounts() throws AppBaseException {
        return DTOConverter.createAccountsClientsListDTOFromEntity(clientFacade.findAll());
    }
    
    
    @RolesAllowed({"Client", "Employee", "Administrator"})
    public AccountDTO downloadMyAccountDTO() throws AppBaseException {
        return DTOConverter.createAccountDTOFromEntity(downloadMyAccount());
    }

    @RolesAllowed({"Client", "Employee", "Administrator"})
    public Account downloadMyAccount() throws AppBaseException {
        return accountFacade.findByLogin(downloadMyLogin());
    }

    @RolesAllowed({"Client", "Employee", "Administrator"})
    public String downloadMyLogin() throws IllegalStateException {
        return sessionContext.getCallerPrincipal().getName();
    }

    @RolesAllowed({"Client"})
    public Client downloadMyAccountClient() {
        return accountFacade.findLoginLikeClient(downloadMyLogin());
    }

    @RolesAllowed({"Employee"})
    public Employee downloadMyAccountEmployee() {
        return accountFacade.findLoginLikeEmployee(downloadMyLogin());
    }

    @RolesAllowed({"Administrator"})
    public Administrator downloadMyAccountAdministrator() {
        return accountFacade.findLoginLikeAdministrator(downloadMyLogin());
    }
    
    @RolesAllowed({"Administrator"})
    public AccountDTO downloadAccountToEditionAndView(AccountDTO account) throws AppBaseException {
        accountState = accountFacade.findByLogin(account.getLogin());
        return DTOConverter.createAccountDTOFromEntity(accountState); 
    }

    @PermitAll
    public AccountDTO rememberSelectedAccountInState(String login) throws AppBaseException {
        accountState = accountFacade.findByLogin(login);
        return DTOConverter.createAccountDTOFromEntity(accountState);
    }

    @RolesAllowed({"Administrator", "Employee", "Client"})
    public AccountDTO rememberMyAccountForDisplayAndEdit() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(downloadMyLogin());
        return DTOConverter.createAccountDTOFromEntity(myAccountState);
    }

    @RolesAllowed({"Administrator", "Employee", "Client"})
    public AccountDTO rememberMyAccountForPasswordChange() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(downloadMyLogin());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword());
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void registerClient(ClientDTO clientDTO) throws AppBaseException {
        Client client = new Client();
        rewriteDataToNewAccount(clientDTO, client);
        client.setCity(clientDTO.getCity());
        client.setStreet(clientDTO.getStreet());
        client.setZipCode(clientDTO.getZipCode());
        client.setHouseNumber(clientDTO.getHouseNumber());
        client.setApartmentNumber(clientDTO.getApartmentNumber());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.createAccount(client);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej registerClient zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed("Administrator")
    public void createAccount(AdministratorDTO administratorDTO) throws AppBaseException {
        Administrator administrator = new Administrator();
        rewriteDataToNewAccount(administratorDTO, administrator);
        administrator.setAlarmNumber(administratorDTO.getAlarmNumber());
        administrator.setCreatedByAdminId(downloadMyAccountAdministrator());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.createAccount(administrator);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej createAccount zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed("Administrator")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createAccount(EmployeeDTO employeeDTO) throws AppBaseException {
        Employee employee = new Employee();
        rewriteDataToNewAccount(employeeDTO, employee);
        employee.setOfficeNumber(employeeDTO.getOfficeNumber());
        employee.setCreatedByAdminId(downloadMyAccountAdministrator());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.createAccount(employee);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej createAccount zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }


    @RolesAllowed({"Client"})
    public void saveMyAccountClientAfterEdition(AccountDTO client) throws AppBaseException {
        if (null == myAccountState) {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
        rewriteEditableDataAccountDTOtoEntity(client, myAccountState);
        ((Client) myAccountState).setCity(((ClientDTO) client).getCity());
        ((Client) myAccountState).setStreet(((ClientDTO) client).getStreet());
        ((Client) myAccountState).setZipCode(((ClientDTO) client).getZipCode());
        ((Client) myAccountState).setHouseNumber(((ClientDTO) client).getHouseNumber());
        ((Client) myAccountState).setApartmentNumber(((ClientDTO) client).getApartmentNumber());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(myAccountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveMyAccountClientAfterEdition zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Administrator"})
    public void saveMyAccountAdministratorAfterEdition(AccountDTO administrator) throws AppBaseException {
        if (null == myAccountState) {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
        rewriteEditableDataAccountDTOtoEntity(administrator, myAccountState);
        ((Administrator) myAccountState).setAlarmNumber(((AdministratorDTO) administrator).getAlarmNumber());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(myAccountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveMyAccountAdministratorAfterEdition zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Employee"})
    public void saveMyAccountEmployeeAfterEdition(AccountDTO employee) throws AppBaseException {
        if (null == myAccountState) {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
        rewriteEditableDataAccountDTOtoEntity(employee, myAccountState);
        ((Employee) myAccountState).setOfficeNumber(((EmployeeDTO) employee).getOfficeNumber());
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(myAccountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveMyAccountEmployeeAfterEdition zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RolesAllowed({"Administrator"})
    public void saveAccountClientAfterEdition(AccountDTO clientDTO) throws AppBaseException {
        if (null == accountState) {
            throw AccountException.createExceptionWrongState(accountState);
        }
        rewriteEditableDataAccountDTOtoEntity(clientDTO, accountState);
        ((Client) accountState).setCity(((ClientDTO) clientDTO).getCity());
        ((Client) accountState).setStreet(((ClientDTO) clientDTO).getStreet());
        ((Client) accountState).setZipCode(((ClientDTO) clientDTO).getZipCode());
        ((Client) accountState).setHouseNumber(((ClientDTO) clientDTO).getHouseNumber());
        ((Client) accountState).setApartmentNumber(((ClientDTO) clientDTO).getApartmentNumber());
        ((Client) accountState).setModifiedBy(downloadMyAccountAdministrator());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.edit(accountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveAccountClientAfterEdition zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RolesAllowed({"Administrator"})
    public void saveAccountEmployeeAfterEdition(AccountDTO employeeDTO) throws AppBaseException {
        if (null == accountState) {
            throw AccountException.createExceptionWrongState(accountState);
        }
        rewriteEditableDataAccountDTOtoEntity(employeeDTO, accountState);
        ((Employee) accountState).setOfficeNumber(((EmployeeDTO) employeeDTO).getOfficeNumber());
        ((Employee) accountState).setModifiedBy(downloadMyAccountAdministrator());
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.edit(accountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveAccountEmployeeAfterEdition zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RolesAllowed({"Administrator"})
    public void saveAccountAdministratorAfterEdition(AccountDTO administratorDTO) throws AppBaseException {
        if (null == accountState) {
            throw AccountException.createExceptionWrongState(accountState);
        }
        rewriteEditableDataAccountDTOtoEntity(administratorDTO, accountState);
        ((Administrator) accountState).setAlarmNumber(((AdministratorDTO) administratorDTO).getAlarmNumber());
        ((Administrator) accountState).setModifiedBy(downloadMyAccountAdministrator());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.edit(accountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveAccountAdministratorAfterEdition zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Administrator"})
    public void activateAccount(String login) throws AppBaseException {
        Account account = accountFacade.findLogin(login);
        if (!account.isActive()) {
            account.setActive(true);
            account.setModifiedBy(downloadMyAccountAdministrator());
        } else {
            throw AccountException.createExceptionAccountAlreadyActvivated(account);
        }

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(account);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej activateAccount zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Administrator"})
    public void deactivateAccount(String login) throws AppBaseException {
        Account account = accountFacade.findLogin(login);
        if (account.isActive()) {
            account.setActive(false);
            account.setModifiedBy(downloadMyAccountAdministrator());
        } else {
            throw AccountException.createExceptionAccountAlreadyDeactvivated(account);
        }

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(account);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej deactivateAccount zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @PermitAll
    public void resetPassword(AccountDTO accountDTO) throws AppBaseException {
        if (accountState.getLogin().equals(accountDTO.getLogin()) && accountState.getAnswer().equals(accountDTO.getAnswer())) {
            accountState.setPassword(hashGenerator.generateHash(accountDTO.getPassword()));
        } else {
            throw AccountException.createExceptionWrongInf(accountState);
        }
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(accountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej resetPassword zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Administrator"})
    public void saveChangePassword(AccountDTO account) throws AppBaseException {
        if (accountState.getLogin().equals(account.getLogin())) {
            accountState.setPassword(hashGenerator.generateHash(account.getPassword()));
            accountState.setModifiedBy(downloadMyAccountAdministrator());

        }
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountManager.edit(accountState);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej changePassword zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Client", "Employee", "Administrator"})
    public void changeMyPassword(String old, String new1) throws AppBaseException {
        Account myAccount = downloadMyAccount();
        if (!myAccount.getPassword().equals(myAccountState.getPassword())) {
            throw AccountException.createExceptionPasswordProvidedSoFarDoesNotMatch(myAccount);
        }
        if (!myAccount.getPassword().equals(hashGenerator.generateHash(old))) {
            throw AccountException.createExceptionWrongPassword(myAccount);
        }
        myAccount.setPassword(hashGenerator.generateHash(new1));

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                accountFacade.edit(myAccount);
                rollbackTX = accountManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej changeMyPassword zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

    
    //Metody narzędziowe
    //Przepisuje do encji te dane konta, które są na formularzu edycji

    private static void rewriteEditableDataAccountDTOtoEntity(AccountDTO accountDTO, Account account) {
        account.setName(accountDTO.getName());
        account.setSurname(accountDTO.getSurname());
        account.setPhone(accountDTO.getPhone());
        account.setEmail(accountDTO.getEmail());
        account.setQuestion(accountDTO.getQuestion());
        account.setAnswer((accountDTO.getAnswer()));
    }

    private void rewriteDataToNewAccount(AccountDTO accountDTO, Account account) {
        account.setLogin(accountDTO.getLogin());
        rewriteEditableDataAccountDTOtoEntity(accountDTO, account);
        account.setActive(true);
        account.setPassword(hashGenerator.generateHash(accountDTO.getPassword()));
    }

}
