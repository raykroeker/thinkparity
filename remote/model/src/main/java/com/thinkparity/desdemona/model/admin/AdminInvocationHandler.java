/*
 * Created On:  9-Oct-07 12:41:45 PM
 */
package com.thinkparity.desdemona.model.admin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.ModelInvocationContext;

/**
 * <b>Title:</b>thinkParity Desdemona Administration Invocation Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class AdminInvocationHandler implements InvocationHandler {

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper(AdminInvocationHandler.class);
    }

    /**
     * Create a new model invocation context.
     * 
     * @param proxy
     *            A proxy <code>Object</code>.
     * @param method
     *            A <code>Method</code>.
     * @param args
     *            The method argument <code>Object[]</code>.
     * @return A <code>ModelInvocationContext</code>.
     */
    private static ModelInvocationContext newContext(final Object proxy,
            final Method method, final Object[] args) {
        final ModelInvocationContext context = new ModelInvocationContext();
        context.setParameterTypes(method.getParameterTypes());
        context.setId(JVMUniqueId.nextId());
        context.setMethod(method);
        return context;
    }

    /** A model. */
    private final AdminModel model;

    /**
     * Create AdminInvocationHandler.
     *
     */
    public AdminInvocationHandler(final AdminModel model) {
        super();
        this.model = model;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        LOGGER.logTrace("Invoking method {0} on {1}.", method, model);
        if (null != args && 0 < args.length && LOGGER.isDebugEnabled()) {
            for (int i = 0; i < args.length; i++)
                LOGGER.logDebug("args[{0}]:{1}", i, args[i]);
        }
        final ModelInvocationContext context = newContext(proxy, method, args);
        ModelInvocationMetrics.begin(context);
        try {
            return LOGGER.logVariable("result", method.invoke(model, args));
        } catch (final InvocationTargetException itx) {
            throw itx.getTargetException();
        } catch (final Throwable t) {
            throw t;
        } finally {
            ModelInvocationMetrics.end(context);
        }
    }
}
