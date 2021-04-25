package pl.lodz.p.it.spjava.fp.boxdietordering.exception;

import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.ClientOrder;

public class ClientOrderException extends AppBaseException {

    static final public String KEY_OPTIMISTIC_LOCK_ORDER = "error.order.optimisticlock";
    static final public String KEY_APROVE_OF_APROVED = "error.clientOrder.aprove.of.aproved";
    static final public String KEY_CLIENTORDER_NOT_FOUND = "error.clientOrder.not.found";

    private ClientOrderException(String message) {
        super(message);
    }

    private ClientOrderException(String message, Throwable cause) {
        super(message, cause);
    }
    private ClientOrder clientOrder;

    public ClientOrder getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(ClientOrder clientOrder) {
        this.clientOrder = clientOrder;
    }

    static public ClientOrderException createClientOrderExceptionWithTxRetryRollback() {
        ClientOrderException oe = new ClientOrderException(KEY_TX_RETRY_ROLLBACK);
        return oe;
    }

    static public ClientOrderException createClientOrderExceptionWithOptimisticLockKey(ClientOrder clientOrder, OptimisticLockException cause) {
        ClientOrderException oe = new ClientOrderException(KEY_OPTIMISTIC_LOCK_ORDER, cause);
        oe.setClientOrder(clientOrder);
        return oe;
    }

    static public ClientOrderException createClientOrderExceptionWithAproveOfAproved(ClientOrder clientOrder) {
        ClientOrderException oe = new ClientOrderException(KEY_APROVE_OF_APROVED);
        oe.setClientOrder(clientOrder);
        return oe;
    }

    static public ClientOrderException createClientOrderExceptionWithClientOrderNotFound() {
        ClientOrderException oe = new ClientOrderException(KEY_CLIENTORDER_NOT_FOUND);
        return oe;
    }

}
