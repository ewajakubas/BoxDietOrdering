package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.DietCategory;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;

@Stateless
@LocalBean
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class DietCategoryFacade extends AbstractFacade<DietCategory> {

    @PersistenceContext(unitName = "pl.lodz.p.it.spjava.boxDietPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DietCategoryFacade() {
        super(DietCategory.class);
    }

    public DietCategory findByName(String name) {
        TypedQuery<DietCategory> tq = em.createNamedQuery("DietCategory.findByName", DietCategory.class);
        tq.setParameter("name", name);
        return tq.getSingleResult();
    }
}
