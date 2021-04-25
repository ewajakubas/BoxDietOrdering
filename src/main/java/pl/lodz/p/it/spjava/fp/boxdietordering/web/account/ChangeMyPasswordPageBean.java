package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AccountException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;

@Named("changeMyPasswordPageBean")
@ViewScoped
public class ChangeMyPasswordPageBean implements Serializable{

    public ChangeMyPasswordPageBean() {
    }

    @Inject
    private AccountController accountController;

    private AccountDTO account = new AccountDTO();

    public AccountDTO getAccount() {
        return account;
    }
    private String passwordRepeat = "";

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    private String oldPassword = "";

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String saveChangedMyPasswordAction() {
        if (!(passwordRepeat.equals(account.getPassword()))) {
            ContextUtils.emitI18NMessage("changeMyPasswordForm:passwordRepeat", "passwords.not.matching");
            return null;
        }
        return accountController.changeMyPassword(oldPassword,account.getPassword());
    }


    @PostConstruct
    public void init() {
        try {
            account = accountController.getMyAccountDTOForPasswordChange();
        } catch (AppBaseException ex) {
            Logger.getLogger(ChangeMyPasswordPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        accountController.getMyAccount().setPassword("");
        }
    }

