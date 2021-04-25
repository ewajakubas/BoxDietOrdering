package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;


@Named
@RequestScoped
public class RegisterClientConfirmPageBean {
    
    public RegisterClientConfirmPageBean() {
    }
    
    @Inject
    private AccountController accountController;

    @PostConstruct
    private void init() {
        account = accountController.getClientRegister();        
    }
    
    private ClientDTO account;

    public ClientDTO getAccount() {
        return account;
    }
    
     public String register() {
        return accountController.registerClient();
    }

}
