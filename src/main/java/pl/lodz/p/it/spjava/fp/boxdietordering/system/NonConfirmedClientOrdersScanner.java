package pl.lodz.p.it.spjava.fp.boxdietordering.system;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.ClientOrderFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.ClientOrder;

@Singleton
@Startup
public class NonConfirmedClientOrdersScanner {

    private static final Logger LOG = Logger.getLogger(NonConfirmedClientOrdersScanner.class.getName());
    private static final int NOT_CONFIRMED_CLIENTORDERS_EXPIRATION_TIMEOUT_IN_HOURS = 72;

    @Inject
    private ClientOrderFacade clientOrderFacade;

    @Schedule(minute = "0", hour = "12")
    private void removeNotConfirmedClientOrders() throws AppBaseException {
        List<ClientOrder> expiredClientOrders = clientOrderFacade.findClientOrderOlderThan(NOT_CONFIRMED_CLIENTORDERS_EXPIRATION_TIMEOUT_IN_HOURS);
        LOG.log(Level.INFO, "{0} obtained {1} expired clientOrders:", new Object[]{this.getClass().getSimpleName(), String.valueOf(expiredClientOrders.size())});
        for (ClientOrder expiredClientOrder : expiredClientOrders) {

            LOG.log(Level.INFO, "   {0}: expired clientOrder {1}: ", new Object[]{this.getClass().getSimpleName(), expiredClientOrder});
            clientOrderFacade.remove(expiredClientOrder);
        }
    }
}