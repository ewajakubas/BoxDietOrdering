package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.managers;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.AccountEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.DietFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.DietException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class DietManager extends AbstractManager {

    @Inject
    private DietFacade dietFacade;

    @Inject
    private AccountEndpoint accountEndpoint;

    @RolesAllowed({"Employee"})
    public void createDiet(Diet diet) throws AppBaseException {
        Employee myAccountEmployee = accountEndpoint.downloadMyAccountEmployee();
        diet.setCreatedByEmployeeId(myAccountEmployee);
        dietFacade.create(diet);
    }

    @RolesAllowed({"Employee"})
    public void deleteDiet(Diet dietState) throws AppBaseException {
        if (null == dietState) {
            throw DietException.createExceptionNoDietFound();
        } else {
            dietFacade.remove(dietState);
        }
    }

    @RolesAllowed({"Employee"})
    public void editDiet(Diet dietState) throws AppBaseException {
        Employee myAccountEmployee = accountEndpoint.downloadMyAccountEmployee();
        dietState.setModifiedByEmployeeId(myAccountEmployee);
        dietFacade.edit(dietState);
    }

}
