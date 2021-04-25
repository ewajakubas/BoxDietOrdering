package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.AccountUtils;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;


@Named(value = "editMyAccountPageBean")
@ViewScoped
public class EditMyAccountPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountController accountController;


    public EditMyAccountPageBean() {
    }

    @PostConstruct
    public void init() {
        try {
            account = accountController.getMyAccountDTOForEdit();
        } catch (AppBaseException ex) {
            Logger.getLogger(EditMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        accountController.getMyAccount().setPassword("");
    }
private AccountDTO account = new AccountDTO();

    public AccountDTO getAccount() {
        return account;
    }

    public boolean isClient() {
        return AccountUtils.isClient(account);
    }

    public boolean isEmployee() {
        return AccountUtils.isEmployee(account);
    }

    public boolean isAdministrator() {
        return AccountUtils.isAdministrator(account);
    }

    public String saveMyAccountClient() throws AppBaseException {
        return accountController.saveMyAccountClientAfterEdition(account);

    }

    public String saveMyAccountEmployee() throws AppBaseException {
        return accountController.saveMyAccountEmployeeAfterEdition(account);

    }

    public String saveMyAccountAdministrator() throws AppBaseException {
        return accountController.saveMyAccountAdministratorAfterEdition(account);

    }
}


