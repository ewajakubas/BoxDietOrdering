package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;


@Named("registerClientPageBean")
@RequestScoped
public class RegisterClientPageBean {

    @EJB
    AccountEndpoint accountEndpoint;

    @Inject
    private AccountController accountController;

    public RegisterClientPageBean() {
    }

    private ClientDTO account = new ClientDTO();

    public ClientDTO getAccount() {
        return account;
    }
    private String passwordRepeat = "";

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String confirmRegister() {
        if (!(passwordRepeat.equals(account.getPassword()))) {
            ContextUtils.emitI18NMessage("registerClientForm:passwordRepeat", "passwords.not.matching");
            return null;
        }

        return accountController.confirmRegisterClient(account);
    }

}
