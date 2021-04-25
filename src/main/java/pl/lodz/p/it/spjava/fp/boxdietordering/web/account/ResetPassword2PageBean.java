package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;

@Named(value = "resetPassword2PageBean")
@RequestScoped
public class ResetPassword2PageBean {

    @Inject
    private AccountController accountController;

    private AccountDTO account;

    private String newPasswordRepeat;

    public ResetPassword2PageBean() {
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public String getNewPasswordRepeat() {
        return newPasswordRepeat;
    }

    public void setNewPasswordRepeat(String newPasswordRepeat) {
        this.newPasswordRepeat = newPasswordRepeat;
    }

    @PostConstruct
    public void init() {
        account = accountController.getSelectedAccount();
    }

    public String resetPasswordAction() {
     if (!newPasswordRepeat.equals(account.getPassword())) { 
         ContextUtils.emitI18NMessage("resetPassword2Form:newPasswordRepeat", "passwords.not.matching");
            return null;
     } 
     return accountController.resetPassword(account); 
    }

}
