package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades;

import java.sql.SQLNonTransientConnectionException;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AccountException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account_;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Administrator;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Client;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;

@Stateless
@LocalBean
@PermitAll
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "pl.lodz.p.it.spjava.boxDietPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    @PermitAll
    public void create(Account entity) throws AppBaseException {
        try {
            super.create(entity);
            em.flush();
        } catch (PersistenceException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_LOGIN)) {
                throw AccountException.createExceptionLoginAlreadyExists(ex, entity);
            } else if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_PERSONALDATA_EMAIL)) {
                throw AccountException.createExceptionEmailAlreadyExists(ex, entity);
            }
        }
    }

    @Override
    public void edit(Account entity) throws AppBaseException {
        try {
            super.edit(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw AccountException.createAccountExceptionWithOptimisticLockKey(entity, oe);
        } catch (PersistenceException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_PERSONALDATA_EMAIL)) {
                throw AccountException.createExceptionEmailAlreadyExists(ex, entity);
            }
        }
    }

    @RolesAllowed({"Client"})
    public Client findLoginLikeClient(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> from = query.from(Client.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get("login"), login));
        TypedQuery<Client> tq = em.createQuery(query);
        return tq.getSingleResult();
    }

    @RolesAllowed({"Employee"})
    public Employee findLoginLikeEmployee(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> from = query.from(Employee.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get("login"), login));
        TypedQuery<Employee> tq = em.createQuery(query);
        return tq.getSingleResult();
    }

    @RolesAllowed({"Administrator"})
    public Administrator findLoginLikeAdministrator(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Administrator> query = cb.createQuery(Administrator.class);
        Root<Administrator> from = query.from(Administrator.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get("login"), login));
        TypedQuery<Administrator> tq = em.createQuery(query);
        return tq.getSingleResult();
    }

    public Account findByLogin(String login) throws AppBaseException {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByLogin", Account.class);
        tq.setParameter("login", login);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw AccountException.createExceptionAccountNotFound(e);
       }
    }
    public Account findLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> from = query.from(Account.class);
        query = query.select(from);
        query = query.where(cb.equal(from.get(Account_.login), login));
        TypedQuery<Account> tq = em.createQuery(query);
        return tq.getSingleResult();
 
    }
}
