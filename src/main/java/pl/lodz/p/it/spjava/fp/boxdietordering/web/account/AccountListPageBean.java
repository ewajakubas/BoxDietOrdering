package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;

@ViewScoped
@Named("accountListPageBean")
public class AccountListPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountController accountController;

    private List<AccountDTO> listAccounts;

    private DataModel<AccountDTO> dataModelAccounts;

    public DataModel<AccountDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    public AccountListPageBean() {
    }

    @PostConstruct
    private void initListAccounts() {
        try {
            listAccounts = accountEndpoint.listAllAccount();
        } catch (AppBaseException ex) {
            Logger.getLogger(AccountListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listAccounts);
    }

    public String activateAccountAction(AccountDTO account) {
        return accountController.activateAccount(account);
    }

    public String deactivateAccountAction(AccountDTO account) {
        return accountController.deactivateAccount(account);
    }

    public String editAccount(AccountDTO account) throws AppBaseException {
        return accountController.downloadAccountToEdition(account);
    }

    public String changePasswordAction(AccountDTO account) throws AppBaseException {
        return accountController.downloadAccountToChangePassword(account);
    }

}
