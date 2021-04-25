package pl.lodz.p.it.spjava.fp.boxdietordering.web.account;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.AccountUtils;


@Named("editAccountPageBean")
@RequestScoped
public class EditAccountPageBean {

    public EditAccountPageBean() {
    }

    @Inject
    private AccountController accountController;

    @PostConstruct
    private void init() {
        account = accountController.getAccountEdit();
    }

    private AccountDTO account = new AccountDTO();

    public AccountDTO getAccount() {
        return account;
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

    public String saveAccountClient() throws AppBaseException {
        return accountController.saveAccountClientAfterEdition(account);

    }

    public String saveAccountEmployee() throws AppBaseException {
        return accountController.saveAccountEmployeeAfterEdition(account);

    }

    public String saveAccountAdministrator() throws AppBaseException {
        return accountController.saveAccounAdministratorAfterEdition(account);

    }
}
