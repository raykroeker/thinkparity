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

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity CommonModel Transaction<br>
 * <b>Description:</b>A think parity model transaction. The transaction is a
 * simple wrapper around a JTA user transaction. The static context design
 * enforces a single active transaction per classloader.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Transaction {

    /** A <code>TransactionContext</code>. */
    private static TransactionContext context;

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
        Assert.assertIsNull(Transaction.context,
                "Cannot nest transaction within context {0}.",
                Transaction.context);
        userTransaction.begin();
        Transaction.context = context;
    }

    /**
     * Determine if this transaction belongs to the context.
     * 
     * @param context
     *            A <code>TransactionContext</code>.
     * @return True if the transaction belongs to this context.
     */
    public Boolean belongsTo(final TransactionContext context) {
        return context.equals(Transaction.context);
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
        try {
            userTransaction.commit();
        } finally {
            Transaction.context = null;
        }
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
     * Obtain the transaction status.
     * 
     * @return A integer representing the transaction status.
     * @throws SystemException
     */
    public int getStatus() throws SystemException {
        return userTransaction.getStatus();
    }

    /**
     * Determine if the transaction is active.
     * 
     * @return True if the transaction is active.
     */
    public Boolean isActive() throws SystemException {
        return Boolean.valueOf(Status.STATUS_ACTIVE == userTransaction.getStatus());
    }

    /**
     * Determine if the transaction's rollback only flag is set.
     * 
     * @return True if the rollback only flag is set.
     * @throws SystemException
     */
    public Boolean isRollbackOnly() throws SystemException {
        return Boolean.valueOf(Status.STATUS_MARKED_ROLLBACK == userTransaction.getStatus());
    }

    /**
     * Attempt to rollback the transaction.
     * 
     * @throws SystemException
     */
    public void rollback() throws SystemException {
        try {
            userTransaction.rollback();
        } finally {
            Transaction.context = null;
        }
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
