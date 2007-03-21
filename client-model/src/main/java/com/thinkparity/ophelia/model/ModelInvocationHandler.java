/*
 * Created On:  9-Jan-07 1:20:00 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import com.thinkparity.codebase.StringUtil.Separator;
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

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    /** The current transaction context <code>Map</code>. */
    private static Map<Object, Object> XA_CONTEXT;

    /** A transaction specific <code>Log4JWrapper</code>. */
    private static final Log4JWrapper XA_LOGGER;

    static {
        XA_CONTEXT = new Hashtable<Object, Object>();
        LOGGER = new Log4JWrapper(ModelInvocationHandler.class);
        XA_LOGGER = new Log4JWrapper("XA_DEBUGGER");
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
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     * 
     */
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        LOGGER.logTrace("Invoking method {0} on {1} in workspace {2}.", method,
                model, workspace);
        synchronized (workspace) {
            final Transaction transaction = workspace.getTransaction();
            final TransactionContext transactionContext = newXAContext(method);
            beginXA(transaction, transactionContext);
            try {
                final Object result;
                try {
                    model.setInvocationContext(new ModelInvocationContext() {
                        public Object[] getArguments() {
                            return args;
                        }
                        public Method getMethod() {
                            return method;
                        }
                    });
                    result = method.invoke(model, args);
                } finally {
                    model.notifyListeners();
                }
                return result;
            } catch (final InvocationTargetException itx) {
                rollbackXA(transaction, transactionContext);
                throw itx.getTargetException();
            } catch (final Throwable t) {
                rollbackXA(transaction, transactionContext);
                throw t;
            } finally {
                completeXA(transaction, transactionContext);
            }
        }
    }

    /**
     * Begin a transaction within a context if required. The context will define
     * a transaction type and if the type requires a transaction one will be
     * begun.
     * 
     * @param transaction
     *            A <code>Transaction</code>.
     * @param context
     *            A <code>TransactionContext</code>.
     * @throws NamingException
     * @throws NotSupportedException
     * @throws SystemException
     */
    private void beginXA(final Transaction transaction,
            final TransactionContext context) throws NotSupportedException,
            SystemException {
        XA_LOGGER.logVariable("transaction", transaction);
        XA_LOGGER.logVariable("transaction.getStatus()", transaction.getStatus());
        XA_LOGGER.logVariable("context", context);
        /* when the transaction context is set, nothing is done
         * 
         * when the transaction context is null, no transaction boundary is
         * currently set, so we need to check whether nor not to begin the
         * transaction based upon the type */
        if (isSetXAContext()) {
            switch (context.getType()) {
            case REQUIRED:
                XA_LOGGER.logInfo("{0}Join {1} with {2}.", "\t\t", context, getXAContext());
                break;
            case REQUIRES_NEW:
                LOGGER.logFatal("New transaction required-{0}", context);
                XA_LOGGER.logFatal("New transaction required-{0}", context);
                Assert.assertUnreachable("New transaction required-{0}", context);
                break;
            case NEVER:
                XA_LOGGER.logInfo("{0}No transaction participation-{1}.", "\t\t", context);
                break;
            case SUPPORTED:
                break;
            default:
                LOGGER.logFatal("Unknown transaction type.");
                XA_LOGGER.logFatal("Unknown transaction type.");
                Assert.assertUnreachable("Unknown transaction type.");
            }
        } else {
            switch (context.getType()) {
            case REQUIRES_NEW:
            case REQUIRED:
                setXAContext(context);
                transaction.begin();
                XA_LOGGER.logInfo("Begin transaction-{0}.", context);
                XA_LOGGER.logVariable("transaction.getStatus()", transaction.getStatus());
                break;
            case NEVER:
                XA_LOGGER.logInfo("{0}No transaction participation-{1}.", "\t\t", context);
                break;
            case SUPPORTED:
                break;
            default:
                LOGGER.logFatal("Unknown transaction type.");
                XA_LOGGER.logFatal("Unknown transaction type.");
                Assert.assertUnreachable("Unknown transaction type.");
            }
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
    private void completeXA(final Transaction transaction,
            final TransactionContext context) throws HeuristicMixedException,
            HeuristicRollbackException, RollbackException, SystemException {
        if (isSetXAContext() && getXAContext().equals(context)) {
            unsetXAContext();
            if (transaction.isRollbackOnly()) {
                XA_LOGGER.logInfo("Rolling transaction-{0}.", context);
                transaction.rollback();
            } else {
                XA_LOGGER.logInfo("Committing transaction-{0}.", context);
                transaction.commit();
            }
        } else {
            switch (context.getType()) {
            case REQUIRES_NEW:
            case REQUIRED:
            case SUPPORTED:
                break;
            case NEVER:
                XA_LOGGER.logInfo("No transaction participation-{0}.", context);
                break;
            default:
                LOGGER.logFatal("Unknown transaction type.");
                XA_LOGGER.logFatal("Unknown transaction type.");
                Assert.assertUnreachable("Unknown transaction type.");
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
    private TransactionType extractXAType(final Method method) {
        ThinkParityTransaction transaction = method.getAnnotation(ThinkParityTransaction.class);
        if (null == transaction) {
            transaction = method.getDeclaringClass().getAnnotation(ThinkParityTransaction.class);
        }
        if (null == transaction) {
            LOGGER.logFatal("Method {0} of class {1} does not define transactional behaviour.",
                    method.getName(), method.getDeclaringClass().toString());
            XA_LOGGER.logFatal("Method {0} of class {1} does not define transactional behaviour.",
                    method.getName(), method.getDeclaringClass().toString());
        }
        Assert.assertNotNull(transaction,
                "Method {0} of class {1} does not define transactional behaviour.",
                method.getName(), method.getDeclaringClass().toString());
        return transaction.value();
    }

    /**
     * Obtain the transaction context. The transaction manager implementation
     * ties a transaction to a thread so the context is also tied to the current
     * thread.
     * 
     * @return A <Code>TransactionContext</code>.
     */
    private TransactionContext getXAContext() {
        return (TransactionContext) XA_CONTEXT.get(getXAThread());
    }

    /**
     * Obtain the execution thread.
     * 
     * @return A <code>Thread</code>.
     */
    private Thread getXAThread() {
        return Thread.currentThread();
    }

    /**
     * Determine if the transaction context is set.
     * 
     * @return True if the transaction context is set.
     */
    private boolean isSetXAContext() {
        return XA_CONTEXT.containsKey(getXAThread());
    }

    /**
     * Create a new transaction context for a given method.
     * 
     * @param method
     *            A <code>Method</code>.
     * @return A <code>TransactionContext</code>.
     */
    private TransactionContext newXAContext(final Method method) {
        final TransactionContextImpl context = new TransactionContextImpl();
        final String xid = new StringBuffer(method.getDeclaringClass().getName())
            .append('_')
            .append(method.getName())
            .toString();
        context.setXid(xidFactory.createXid(xid));
        context.setType(extractXAType(method));
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
    private void rollbackXA(final Transaction transaction,
            final TransactionContext context) throws SystemException {
        switch (context.getType()) {
        case REQUIRES_NEW:
        case REQUIRED:
            XA_LOGGER.logInfo("Set rollback only-{0}.", context);
            transaction.setRollbackOnly();
            break;
        case NEVER:
            XA_LOGGER.logInfo("No transaction participation-{0}.", context);
            break;
        case SUPPORTED:
            if (transaction.isActive()) {
                XA_LOGGER.logInfo("Set rollback only-{0}.", context);
                transaction.setRollbackOnly();
            } else {
                XA_LOGGER.logTrace("Transaction for {0} is not active.", context);
            }
            break;
        default:
            LOGGER.logFatal("Unknown transaction type.");
            XA_LOGGER.logFatal("Unknown transaction type.");
            Assert.assertUnreachable("Unknown transaction type.");
        }
    }

    /**
     * Set the transaction context.
     * 
     * @param context
     *            A <code>TransactionContext</code>.
     */
    private void setXAContext(final TransactionContext context) {
        final TransactionContext currentContext = getXAContext();
        if (null == currentContext) {
            XA_CONTEXT.put(getXAThread(), context);
        } else {
            LOGGER.logFatal("Context already set.{0}  current:{1}{0}  context:{2}",
                    Separator.SystemNewLine, currentContext, context);
            XA_LOGGER.logFatal("Context already set.{0}  current:{1}{0}  context:{2}",
                    Separator.SystemNewLine, currentContext, context);
        }
    }

    /**
     * Unset the current transaction context.
     *
     */
    private void unsetXAContext() {
        final TransactionContext currentContext = getXAContext();
        if (null == currentContext) {
            LOGGER.logFatal("Context not set.");
            XA_LOGGER.logFatal("Context not set.");
        } else {
            XA_CONTEXT.remove(getXAThread());
        }
    }
}
