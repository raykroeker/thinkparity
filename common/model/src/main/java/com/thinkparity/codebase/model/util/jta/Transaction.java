/*
 * Created On:  5-Jan-07 1:06:23 PM
 */
package com.thinkparity.codebase.model.util.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * <b>Title:</b>thinkParity Model Transaction<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Transaction {

    /** A <code>TransactionContext</code>. */
    private TransactionContext context;

    /** The jta <code>UserTransaction</code>. */
    private final UserTransaction userTransaction;

    /**
     * Create Transaction. Uses a default method transaction boundary.
     * 
     * @param userTransaction
     *            A jta <code>UserTransaction</code>.
     */
    Transaction(final UserTransaction userTransaction) {
        super();
        this.userTransaction = userTransaction;
    }

    /**
     * Begin the transaction.
     * 
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void begin(final TransactionContext context)
            throws NotSupportedException, SystemException {
        userTransaction.begin();
        this.context = context;
    }

    /**
     * Determine if this transaction belongs to the context.
     * 
     * @param context
     *            A <code>TransactionContext</code>.
     * @return True if the transaction belongs to this context.
     */
    public Boolean belongsTo(final TransactionContext context) {
        return context.equals(this.context);
    }

    /**
     * Attempt to commit the transaction.
     * 
     * @throws HeuristicMixedException
     * @throws HeuristicRollbackException
     * @throws RollbackException
     * @throws SystemException
     */
    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, RollbackException, SystemException {
        userTransaction.commit();
    }

    /**
     * Obtain context.
     * 
     * @return A <code>TransactionContext</code>.
     */
    public TransactionContext getContext() {
        return context;
    }

    /**
     * Determine if the transaction is active.
     * 
     * @return True if the transaction is active.
     */
    public Boolean isActive() throws SystemException {
        return Status.STATUS_ACTIVE == userTransaction.getStatus();
    }

    /**
     * Determine if the transaction's rollback only flag is set.
     * 
     * @return True if the rollback only flag is set.
     * @throws SystemException
     */
    public Boolean isRollbackOnly() throws SystemException {
        return userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK;
    }

    /**
     * Attempt to rollback the transaction.
     * 
     * @throws SystemException
     */
    public void rollback() throws SystemException {
        userTransaction.rollback();
    }

    /**
     * Set the rollback only flag.
     * 
     * @throws SystemException
     */
    public void setRollbackOnly() throws SystemException {
        userTransaction.setRollbackOnly();
    }
}
