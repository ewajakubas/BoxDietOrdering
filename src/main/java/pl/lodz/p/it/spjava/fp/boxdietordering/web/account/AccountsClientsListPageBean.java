package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;

@RequestScoped
@Named("accountsClientsListPageBean")
public class AccountsClientsListPageBean implements Serializable {

    public AccountsClientsListPageBean() {
    }
    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountController accountController;

    private List<ClientDTO> listAccounts;

    private DataModel<ClientDTO> dataModelAccounts;

    public DataModel<ClientDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    @PostConstruct
    private void initListClientsAccounts() {
        try {
            listAccounts = accountEndpoint.listClientsAccounts();
        } catch (AppBaseException ex) {
            Logger.getLogger(AccountsClientsListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listAccounts);
    }


}