package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;


@Named(value = "resetPasswordPageBean")
@RequestScoped
public class ResetPasswordPageBean {
      
    @Inject
    private AccountController accountController;

    private AccountDTO account = new AccountDTO();

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
    
    public ResetPasswordPageBean() {
    }

    public String selectAccountToResetPasswordAction() {
        try {
            accountController.selectAccountForChange(account);
        } catch (AppBaseException ex) {
            Logger.getLogger(ResetPasswordPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        accountController.getSelectedAccount().setAnswer("");
        return "resetPassword2";
    }

}
