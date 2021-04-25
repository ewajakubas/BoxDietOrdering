package pl.lodz.p.it.spjava.fp.boxdietordering.exception;

import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;

public class DietException extends AppBaseException {

    static final public String KEY_DIET_NAME_EXISTS = "error.diet.name.exists";
    static final public String KEY_DIET_WRONG_STATE = "error.diet.wrong.state";
    static final public String KEY_DIET_NOT_FOUND = "error.diet.no.found";
    static final public String KEY_DIET_OPTIMISTIC_LOCK = "error.diet.optimisticlock";
    static final public String KEY_DIET_ALREADY_CHANGED = "error.diet.already.changed";
    static final public String KEY_ORDERED_DIET_IS = "error.diet.is.in.order";
    
    private DietException(String message) {
        super(message);
    }

    private DietException(String message, Diet diet) {
        super(message);
        this.diet = diet;
    }

    private DietException(String message, Throwable cause, Diet diet) {
        super(message, cause);
        this.diet = diet;
    }

    private Diet diet;

    private DietException(String message, Throwable cause) {
        super(message, cause); 
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }
    
    static public DietException createDietExceptionWithTxRetryRollback() {
        DietException de = new DietException(KEY_TX_RETRY_ROLLBACK);
        return de;
    }
    
    static public DietException createExceptionDietNameExists(Throwable cause, Diet diet) {
        return new DietException(KEY_DIET_NAME_EXISTS, cause, diet);    
    }
    
    public static DietException createExceptionWrongState(Diet diet) {
        return new DietException(KEY_DIET_WRONG_STATE, diet);
    }
    public static DietException createExceptionNoDietFound() {
        return new DietException(KEY_DIET_NOT_FOUND);
    }
    
    static public DietException createDietExceptionWithOptimisticLockKey(Diet diet, OptimisticLockException cause) {
        DietException de = new DietException(KEY_DIET_OPTIMISTIC_LOCK, cause);
        de.setDiet(diet);
        return de;
    }
        static public DietException ceateExceptionDietChangedByAnotherAdmin(Diet diet) {
        return new DietException(KEY_DIET_ALREADY_CHANGED, diet);
    }
            static public DietException createExceptionDietInOrder(Throwable cause, Diet diet) {
        return new DietException(KEY_ORDERED_DIET_IS, cause, diet);
    }
    
}

