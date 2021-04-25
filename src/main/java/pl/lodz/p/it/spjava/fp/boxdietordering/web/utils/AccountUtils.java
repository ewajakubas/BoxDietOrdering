package pl.lodz.p.it.spjava.fp.boxdietordering.web.utils;

import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AdministratorDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.EmployeeDTO;

public class AccountUtils {

    public static boolean isAdministrator(AccountDTO account) {
        return (account instanceof AdministratorDTO);
    }

    public static boolean isEmployee(AccountDTO account) {
        return (account instanceof EmployeeDTO);
    }

    public static boolean isClient(AccountDTO account) {
        return (account instanceof ClientDTO);
    }
 
}

