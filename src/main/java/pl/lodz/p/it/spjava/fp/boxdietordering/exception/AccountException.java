package pl.lodz.p.it.spjava.fp.boxdietordering.exception;

import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account;

public class AccountException extends AppBaseException {

    static final public String KEY_ACCOUNT_NOT_EXISTS = "error.account.not.exists";
    static final public String KEY_ACCOUNT_LOGIN_EXISTS = "error.account.login.exists";
    static final public String KEY_ACCOUNT_EMAIL_EXISTS = "error.account.email.exists";
    static final public String KEY_ACCOUNT_WRONG_STATE = "error.account.wrong.state";
    static final public String KEY_ACCOUNT_WRONG_INF = "error.account.wrong.inf";
    static final public String KEY_NO_ACCOUNT_FOUND = "error.no.account.found";
    static final public String KEY_ACCOUNT_WRONG_PASSWORD = "error.account.wrong.password";
    static final public String KEY_ACCOUNT_PASSWORD_DOESNOT_MATCH = "error.account.password.provided.so.far.does.not.match";
    static final public String KEY_ACCOUNT_OPTIMISTIC_LOCK = "error.account.optimisticlock";
    static final public String KEY_ACCOUNT_ALREADY_ACTIVATED = "error.account.already.active";
    static final public String KEY_ACCOUNT_ALREADY_DEACTIVATED = "error.account.already.deactive";

    private AccountException(String message, Account account) {
        super(message);
        this.account = account;
    }

    private AccountException(String message, Throwable cause, Account account) {
        super(message, cause);
        this.account = account;
    }

    private AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    static public AccountException createAccountExceptionWithTxRetryRollback() {
        AccountException ae = new AccountException(KEY_TX_RETRY_ROLLBACK);
        return ae;
    }

    static public AccountException createExceptionAccountNotExists(NoResultException e) {
        return new AccountException(KEY_ACCOUNT_NOT_EXISTS, e);
    }

    static public AccountException createExceptionLoginAlreadyExists(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_LOGIN_EXISTS, cause, account);
    }

    static public AccountException createExceptionEmailAlreadyExists(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_EMAIL_EXISTS, cause, account);
    }

    static public AccountException createExceptionWrongState(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_STATE, account);
    }
    static public AccountException createExceptionWrongInf(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_INF, account);
    }
        public static AccountException createExceptionAccountNotFound(NoResultException e) {
        return new AccountException(KEY_NO_ACCOUNT_FOUND, e);
    } 
    static public AccountException createExceptionWrongPassword(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_PASSWORD, account);
    }
    static public AccountException createExceptionPasswordProvidedSoFarDoesNotMatch(Account account) {
        return new AccountException(KEY_ACCOUNT_PASSWORD_DOESNOT_MATCH, account);
    }

    static public AccountException createAccountExceptionWithOptimisticLockKey(Account account, OptimisticLockException cause) {
        AccountException ae = new AccountException(KEY_ACCOUNT_OPTIMISTIC_LOCK, cause);
        ae.setAccount(account);
        return ae;
    }

    static public AccountException createExceptionAccountAlreadyActvivated(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_ACTIVATED, account);
    }

    static public AccountException createExceptionAccountAlreadyDeactvivated(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_DEACTIVATED, account);
    }
}
