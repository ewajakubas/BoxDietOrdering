package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AdministratorDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;


@Named("createAdministratorPageBean")
@RequestScoped
public class CreateAdministratorPageBean {

    
    public CreateAdministratorPageBean() {
    }
    
    @Inject
    private AccountController accountController;

    private AdministratorDTO account =  new AdministratorDTO();

    public AdministratorDTO getAccount() {
        return account;
    }

    private String passwordRepeat = "";

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
    
    public String create() {
        if (!(passwordRepeat.equals(account.getPassword()))) {
            ContextUtils.emitI18NMessage("createAdministratorForm:passwordRepeat", "passwords.not.matching");
            return null;
        }   
        return accountController.createAdministrator(account);
    }
}

