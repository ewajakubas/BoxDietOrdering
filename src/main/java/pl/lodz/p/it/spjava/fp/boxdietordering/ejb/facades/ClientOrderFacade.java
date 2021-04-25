package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades;

import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.ClientOrder;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.ClientOrderException;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class ClientOrderFacade extends AbstractFacade<ClientOrder> {

    @PersistenceContext(unitName = "pl.lodz.p.it.spjava.boxDietPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientOrderFacade() {
        super(ClientOrder.class);
    }

    @RolesAllowed({"Employee"})
    public List<ClientOrder> findNotAcceptedClientOrders() {
        TypedQuery<ClientOrder> tq = em.createNamedQuery("ClientOrder.findNotAccepted", ClientOrder.class);
        return tq.getResultList();
    }
    
    @RolesAllowed({"Employee"})
    public List<ClientOrder> findAcceptedClientOrders() {
        TypedQuery<ClientOrder> tq = em.createNamedQuery("ClientOrder.findAccepted", ClientOrder.class);
        return tq.getResultList();
    }

    @RolesAllowed({"Client"})
    public List<ClientOrder> findClientOrdersForClient(String login) {
        TypedQuery<ClientOrder> tq = em.createNamedQuery("ClientOrder.findForClient", ClientOrder.class);
        tq.setParameter("login", login);
        return tq.getResultList();
    }

    @RolesAllowed({"Client"})
    public List<ClientOrder> findClientOrdersNotAcceptedForClient(String login) {
        TypedQuery<ClientOrder> tq = em.createNamedQuery("ClientOrder.findNotAcceptedForClient", ClientOrder.class);
        tq.setParameter("login", login);
        return tq.getResultList();
    }


        public List<ClientOrder> findClientOrderOlderThan(long hours) {
        Date oldest = new Date(new Date().getTime() - 60 * 60 * 1000 * hours);
        String query = "SELECT co from ClientOrder co WHERE co.creationTimestamp < :oldest AND co.accepted=false";
        TypedQuery<ClientOrder> tq = em.createQuery(query, ClientOrder.class);
        tq.setParameter("oldest", oldest);
        return tq.getResultList();
    }

    @Override
    public void edit(ClientOrder entity) throws AppBaseException {
        try {
            super.edit(entity);
            em.flush();
        } catch (OptimisticLockException oe) {
            throw ClientOrderException.createClientOrderExceptionWithOptimisticLockKey(entity, oe);
        } catch (ClientOrderException oe) {
            throw ClientOrderException.createClientOrderExceptionWithAproveOfAproved(entity);
        }
    }
}
