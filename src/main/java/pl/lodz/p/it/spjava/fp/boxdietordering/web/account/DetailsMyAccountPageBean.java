package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.AccountUtils;


@Named("detailsMyAccountPageBean")
@RequestScoped
public class DetailsMyAccountPageBean {
    
    public DetailsMyAccountPageBean() {
    }
    
    @Inject
    private AccountController accountController;

    private AccountDTO account;

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }  
    
    @PostConstruct
    private void init() {
        try {
            account = accountController.downloadMyAccount();
        } catch (AppBaseException ex) {
            Logger.getLogger(DetailsMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}
