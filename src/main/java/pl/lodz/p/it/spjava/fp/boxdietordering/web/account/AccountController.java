package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AdministratorDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.EmployeeDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AccountException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;

@SessionScoped
public class AccountController implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    private ClientDTO clientRegister;

    private AdministratorDTO administratorCreate;

    private EmployeeDTO employeeCreate;

    private AccountDTO accountEdit;

    private AccountDTO accountView;

    private AccountDTO accountChangePassword;

    private AccountDTO selectedAccount;

    private AccountDTO myAccount;

    public AccountDTO getSelectedAccount() {
        return selectedAccount;
    }

    public AccountDTO getMyAccount() {
        return myAccount;
    }

    public AccountDTO getAccountEdit() {
        return accountEdit;
    }

    public AccountDTO getAccountView() {
        return accountView;
    }

    public AccountDTO getAccountChangePassword() {
        return accountChangePassword;
    }

    public ClientDTO getClientRegister() {
        return clientRegister;
    }

    public AccountController() {
    }

    public String registerClient() {
        try {
            accountEndpoint.registerClient(clientRegister);
            clientRegister = null;
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_LOGIN_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("registerClientConfirmForm:login", AccountException.KEY_ACCOUNT_LOGIN_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("registerClientConfirmForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);

            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Zgłoszenie w metodzie akcji registerClient wyjatku: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji registerClient wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String createAdministrator(AdministratorDTO admin) {
        try {
            administratorCreate = admin;
            accountEndpoint.createAccount(administratorCreate);
            administratorCreate = null;
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_LOGIN_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("createAdminForm:login", AccountException.KEY_ACCOUNT_LOGIN_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("createAdminForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Zgłoszenie w metodzie akcji createAdministrator wyjatku: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji createAdministrator wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage()); 
            }
            return null;
        }
    }

    public String createEmployee(EmployeeDTO employee) {
        try {
            employeeCreate = employee;
            accountEndpoint.createAccount(employeeCreate);
            employeeCreate = null;
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_LOGIN_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("createEmployeeForm:login", AccountException.KEY_ACCOUNT_LOGIN_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("createEmployeeForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Zgłoszenie w metodzie akcji createEmployee wyjatku:  ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji createEmployee wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String changeMyPassword(String old, String new1) {
        try {
            accountEndpoint.changeMyPassword(old, new1);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_PASSWORD_DOESNOT_MATCH.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_PASSWORD_DOESNOT_MATCH);
            } else if (AccountException.KEY_ACCOUNT_WRONG_PASSWORD.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_WRONG_PASSWORD);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of changeMyPassword using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception changeMyPassword in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String activateAccount(AccountDTO account) {
        try {
            accountEndpoint.activateAccount(account.getLogin());
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_ALREADY_ACTIVATED.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_ALREADY_ACTIVATED);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of activateAccount using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji activateAccount wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String deactivateAccount(AccountDTO account) {
        try {
            accountEndpoint.deactivateAccount(account.getLogin());
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_ALREADY_DEACTIVATED.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_ALREADY_DEACTIVATED);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of deactivateAccount using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji deactivateAccount wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                System.out.println("jestem tu bez bledu 3");

                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String changePasswordAccount(AccountDTO account) {
        try {
            accountEndpoint.saveChangePassword(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of changePasswordAccount using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception changePasswordAccount in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String saveAccountClientAfterEdition(AccountDTO account) {
        try {
            accountEndpoint.saveAccountClientAfterEdition(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("editAccountForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of saveAccountClientAfterEdition using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception saveAccountClientAfterEdition in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String saveAccountEmployeeAfterEdition(AccountDTO account) {
        try {
            accountEndpoint.saveAccountEmployeeAfterEdition(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("editAccountForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of saveAccountEmployeeAfterEdition using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception type saveAccountEmployeeAfterEdition in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String saveAccounAdministratorAfterEdition(AccountDTO account) {
        try {
            accountEndpoint.saveAccountAdministratorAfterEdition(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("editAccountForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of saveAccounAdministratorAfterEdition  using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception type saveAccounAdministratorAfterEdition in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String saveMyAccountClientAfterEdition(final AccountDTO account) throws AppBaseException {
        try {
            accountEndpoint.saveMyAccountClientAfterEdition(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("editMyAccountForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of saveAccounAdministratorAfterEdition  using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception type saveAccounAdministratorAfterEdition in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String saveMyAccountEmployeeAfterEdition(final AccountDTO account) throws AppBaseException {
        try {
            accountEndpoint.saveMyAccountEmployeeAfterEdition(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("editMyAccountForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of saveMyAccountEmployeeAfterEdition  using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception type saveMyAccountEmployeeAfterEdition in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }
    
        public String saveMyAccountAdministratorAfterEdition(final AccountDTO account) throws AppBaseException {
        try {
            accountEndpoint.saveMyAccountAdministratorAfterEdition(account);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_EMAIL_EXISTS.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage("editMyAccountForm:email", AccountException.KEY_ACCOUNT_EMAIL_EXISTS);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of saveMyAccountAdministratorAfterEdition  using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception type saveMyAccountAdministratorAfterEdition in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String resetPassword(AccountDTO accountDTO) {
        try {
            accountEndpoint.resetPassword(accountDTO);
            return "success";
        } catch (AccountException ae) {
            if (AccountException.KEY_ACCOUNT_WRONG_INF.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_WRONG_INF);
            } else if (AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK.equals(ae.getMessage())) {
                ContextUtils.emitI18NMessage(null, AccountException.KEY_ACCOUNT_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                        "Reporting the exception of resetPassword  using the method: ", ae);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE,
                    "Create an exception type resetPassword in the action method: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }

    }

    public String confirmRegisterClient(ClientDTO client) {
        this.clientRegister = client;
        return "confirmRegister";
    }

    public String downloadAccountToChangePassword(AccountDTO account) throws AppBaseException {
        accountChangePassword = accountEndpoint.downloadAccountToEditionAndView(account);
        return "changePassword";
    }

    public String downloadAccountToEdition(AccountDTO account) throws AppBaseException {
        accountEdit = accountEndpoint.downloadAccountToEditionAndView(account);
        return "editAccount";
    }

    public AccountDTO downloadMyAccount() throws AppBaseException {
        return accountEndpoint.downloadMyAccountDTO();
    }

    public void selectAccountForChange(AccountDTO account) throws AppBaseException {
        selectedAccount = accountEndpoint.rememberSelectedAccountInState(account.getLogin());
    }

    public AccountDTO getMyAccountDTOForEdit() throws AppBaseException {
        myAccount = accountEndpoint.rememberMyAccountForDisplayAndEdit();
        return myAccount;
    }

    public AccountDTO getMyAccountDTOForPasswordChange() throws AppBaseException {
        myAccount = accountEndpoint.rememberMyAccountForPasswordChange();
        return myAccount;
    }

}
