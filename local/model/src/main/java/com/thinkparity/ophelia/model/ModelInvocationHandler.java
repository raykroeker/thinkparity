/*
 * Created On:  9-Jan-07 1:20:00 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.Transaction;
import com.thinkparity.codebase.model.util.jta.TransactionContext;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.util.jta.TransactionContextImpl;
import com.thinkparity.ophelia.model.util.jta.XidFactory;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Model Invocation Handler<br>
 * <b>Description:</b>The invocation handler for the dynamic proxies generated
 * by the model factories. The invocation handler will control all transactions
 * as well as interface and parameter logging.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ModelInvocationHandler implements InvocationHandler {

    /** An apache logger wrapper. */
    private static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper(ModelInvocationHandler.class);
    }

    /** The target <code>Model</code>. */
    private final Model model;

    /** A thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /** An <code>XidFactory</code>. */
    private final XidFactory xidFactory;

    /**
     * Create ModelInvocationHandler.
     *
     */
    ModelInvocationHandler(final Workspace workspace, final Model model) {
        super();
        this.workspace = workspace;
        this.model = model;
        this.xidFactory = XidFactory.getInstance(workspace);
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        LOGGER.logTrace("Invoking method {0} on {1} in workspace {2}.", method,
                model, workspace);
        synchronized (workspace) {
            final Transaction transaction = workspace.getTransaction();
            final TransactionContext transactionContext = newTransactionContext(method);
            beginTransaction(transaction, transactionContext);
            try {
                final Object result;
                try {
                    result = method.invoke(model, args);
                } finally {
                    model.notifyListeners();
                }
                return result;
            } catch (final InvocationTargetException itx) {
                rollbackTransaction(transaction);
                throw itx.getTargetException();
            } catch (final Throwable t) {
                rollbackTransaction(transaction);
                throw t;
            } finally {
                completeTransaction(transaction, transactionContext);
            }
        }
    }

    /**
     * Begin a transaction within a context if required. The context will define
     * a transaction type and if the type requires a transaction one will be
     * begun.
     * 
     * @param transactionContext
     *            A <code>TransactionContext</code>.
     * @throws NamingException
     * @throws NotSupportedException
     * @throws SystemException
     */
    private void beginTransaction(final Transaction transaction,
            final TransactionContext transactionContext)
            throws NotSupportedException, SystemException {
        LOGGER.logVariable("transaction", transaction);
        LOGGER.logVariable("transaction.getContext()", transaction.getContext());
        LOGGER.logVariable("transaction.getStatus()", transaction.getStatus());
        switch (transactionContext.getType()) {
        case REQUIRES_NEW:
            LOGGER.logInfo("Starting transaction for {0}.",
                    transactionContext);
            transaction.begin(transactionContext);
            break;
        case REQUIRED:
            if (!transaction.isActive().booleanValue()) {
                LOGGER.logInfo("Starting transaction for {0}.",
                        transactionContext);
                transaction.begin(transactionContext);
                Assert.assertTrue(transaction.isActive(),
                        "Transaction required for {0}.", transactionContext);
            } else {
                LOGGER.logInfo("{0}Joining transaction with {1}.", "\t", transactionContext);
            }
            break;
        case NEVER:
            Assert.assertNotTrue(transaction.isActive(),
                    "Transaction not permitted for {0}.", transactionContext);
            break;
        case SUPPORTED:
            break;
        default:
            throw Assert.createUnreachable("Unknown transaction type.");
        }
    }

    /**
     * Complete a transaction. If the transaction belongs to the given
     * transaction context a rollback/commit will be attempted.
     * 
     * @param transaction
     *            A <code>Transaction</code>.
     * @param transactionContext
     *            A <code>TransactionContext</code>.
     * @throws HeuristicMixedException
     * @throws HeuristicRollbackException
     * @throws RollbackException
     * @throws SystemException
     */
    private void completeTransaction(final Transaction transaction,
            final TransactionContext transactionContext)
            throws HeuristicMixedException, HeuristicRollbackException,
            RollbackException, SystemException {
        if (transaction.belongsTo(transactionContext)) {
            if (transaction.isRollbackOnly()) {
                LOGGER.logInfo("Rolling back transaction for {0}.", transaction.getContext());
                transaction.rollback();
            } else {
                LOGGER.logInfo("Commiting transaction for {0}.", transaction.getContext());
                transaction.commit();
            }
        } else {
            switch (transactionContext.getType()) {
            case REQUIRES_NEW:
            case REQUIRED:
            case SUPPORTED:
                break;
            case NEVER:
                Assert.assertNotTrue(transaction.isActive(),
                        "Transaction not permitted for {0}.", transactionContext);
                break;
            default:
                throw Assert.createUnreachable("Unknown transaction type.");
            }
        }
    }

    /**
     * Obtain the annotated transaction type for the method. If the transaction
     * type is not defined on the method; attempt to retreive it from the class.
     * If no transaction annotation is found assert.
     * 
     * @param method
     *            A <code>Method</code>.
     * @return A <code>TransactionType</code>.
     */
    private TransactionType getTransactionType(final Method method) {
        ThinkParityTransaction transaction = method.getAnnotation(ThinkParityTransaction.class);
        if (null == transaction) {
            transaction = method.getDeclaringClass().getAnnotation(ThinkParityTransaction.class);
        }
        Assert.assertNotNull(transaction,
                "Method {0} of class {1} does not define transactional behaviour.",
                method.toString(), method.getDeclaringClass().toString());
        return transaction.value();
    }

    /**
     * Create a new transaction context for a given method.
     * 
     * @param method
     *            A <code>Method</code>.
     * @return A <code>TransactionContext</code>.
     */
    private TransactionContext newTransactionContext(final Method method) {
        final TransactionContextImpl context = new TransactionContextImpl();
        final String xid = new StringBuffer(method.getClass().toString())
            .append("_")
            .append(method.toString())
            .toString();
        context.setXid(xidFactory.createXid(xid));
        context.setType(getTransactionType(method));
        return context;
    }

    /**
     * Rollback a transaction. If the transaction belongs to the context; a
     * rollback will be attempted. If it does not; depending on the context
     * type; the transaction's rollback only flag will be set.
     * 
     * @param transaction
     *            A <code>Transaction</code>.
     * @param transactionContext
     *            A <code>TransactionContext</code>.
     * @throws SystemException
     */
    private void rollbackTransaction(final Transaction transaction) throws SystemException {
        switch (transaction.getContext().getType()) {
        case REQUIRES_NEW:
        case REQUIRED:
            LOGGER.logInfo("Set rollback only for {0}.",
                    transaction.getContext());
            transaction.setRollbackOnly();
            break;
        case NEVER:
            Assert.assertNotTrue(transaction.isActive(),
                    "Transaction not permitted for {0}.", transaction.getContext());
            break;
        case SUPPORTED:
            if (transaction.isActive()) {
                LOGGER.logInfo("Set rollback only for {0}.",
                        transaction.getContext());
                transaction.setRollbackOnly();
            } else {
                LOGGER.logTrace("Transaction for {0} is not active.",
                        transaction.getContext());
            }
            break;
        default:
            throw Assert.createUnreachable("Unknown transaction type.");
        }
    }
}
