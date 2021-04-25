package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;


@Named("changePasswordAccountPageBean")
@RequestScoped
public class ChangePasswordAccountPageBean {
    
    public ChangePasswordAccountPageBean() {
    }
    
    @Inject
    private AccountController accountController;
    
    private AccountDTO account;

    public AccountDTO getAccount() {
        return account;
    }  
    
    @PostConstruct
    private void init(){
        account = accountController.getAccountChangePassword();
        System.out.println("accountChangePassword "+ account.getLogin());
    }
    
    private String passwordRepeat = "";

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
  
    public String changePassword(){
        if (!(passwordRepeat.equals(account.getPassword()))){
            ContextUtils.emitI18NMessage("changePasswordAccountForm:passwordRepeat", "passwords.not.matching");
            return null;
        }           
        return accountController.changePasswordAccount(account);
    }
}

    

