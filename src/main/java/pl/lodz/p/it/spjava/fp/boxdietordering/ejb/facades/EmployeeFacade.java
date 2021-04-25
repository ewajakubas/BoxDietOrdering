package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;


@Stateless
@LocalBean
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"Administrator"})
public class EmployeeFacade extends AbstractFacade<Employee> {

    @PersistenceContext(unitName = "pl.lodz.p.it.spjava.boxDietPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }
    
}
