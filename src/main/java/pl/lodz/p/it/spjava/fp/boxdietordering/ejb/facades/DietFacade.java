package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.DietException;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed({"Employee"})
public class DietFacade extends AbstractFacade<Diet> {

    @PersistenceContext(unitName = "pl.lodz.p.it.spjava.boxDietPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DietFacade() {
        super(Diet.class);
    }

    @Override
    public void create(Diet entity) throws AppBaseException {
        try {
            super.create(entity);
            em.flush();
        } catch (PersistenceException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_DIET_NAME)) {
                throw DietException.createExceptionDietNameExists(cause, entity);
            }
        }
    }

    @Override
    public void edit(Diet entity) throws AppBaseException {
        try {
            super.edit(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw DietException.createDietExceptionWithOptimisticLockKey(entity, oe);
        } catch (PersistenceException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_DIET_NAME)) {
                throw DietException.createExceptionDietNameExists(cause, entity);
            }
        }
    }

    @Override
    public void remove(Diet entity) throws AppBaseException {
        try {
            super.remove(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw DietException.createDietExceptionWithOptimisticLockKey(entity, oe);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains("ORDER_ITEM_DIET_ID")) {
                throw DietException.createExceptionDietInOrder(e, entity);
            }
        }
    }

    public Diet findByName(String name) throws AppBaseException {
        TypedQuery<Diet> tq = em.createNamedQuery("Diet.findByName", Diet.class);
        tq.setParameter("name", name);
        return tq.getSingleResult();
    }

    public Diet findById(Long id) throws AppBaseException {
        TypedQuery<Diet> tq = em.createNamedQuery("Diet.findById", Diet.class);
        tq.setParameter("id", id);
        return tq.getSingleResult();
    }
}
