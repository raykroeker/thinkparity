/*
 * Created On:  21-Dec-07 12:50:09 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.workspace.Transaction;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Ophelia Workspace Configuration Proxy<br>
 * <b>Description:</b>Provides synchronization and transaction control for the
 * configuration interface.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ConfigurationProxy implements InvocationHandler {

    /** A log4j wrapper. */
    private static final Log4JWrapper logger;

    static {
        logger = new Log4JWrapper(ConfigurationProxy.class);
    }

    /** The configuration implementation. */
    private final Object impl;

    /** A workspace. */
    private final Workspace workspace;

    /**
     * Create ConfigurationProxy.
     *
     */
    ConfigurationProxy(final Workspace workspace, final Object impl) {
        super();
        this.workspace = workspace;
        this.impl = impl;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    @Override
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        logger.logTrace("Invoking method {0} on {1} in {2}.", method,
                proxy, workspace);
        if (null != args && 0 < args.length && logger.isDebugEnabled()) {
            for (int i = 0; i < args.length; i++)
                logger.logDebug("args[{0}]:{1}", i, args[i]);
        }
        synchronized (workspace) {
            final Transaction transaction = workspace.getTransaction();
            transaction.begin();
            try {
                final Object result = method.invoke(impl, args);
                return logger.logVariable("result", result);
            } catch (final InvocationTargetException itx) {
                transaction.setRollbackOnly();
                throw itx.getTargetException();
            } catch (final Throwable t) {
                transaction.setRollbackOnly();
                throw t;
            } finally {
                if (transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            }
        }
    }
}
