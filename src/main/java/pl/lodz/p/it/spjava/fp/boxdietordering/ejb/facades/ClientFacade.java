/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Client;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;


@Stateless
@LocalBean
@Interceptors(LoggingInterceptor.class)
@RolesAllowed({"Administrator"})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ClientFacade extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "pl.lodz.p.it.spjava.boxDietPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }

    
}
