/*
 * Created On:  6-Jun-07 2:02:11 PM
 */
package com.thinkparity.desdemona.web.service;

import java.lang.reflect.Method;


/**
 * <b>Title:</b>thinkParity Desdemona Web Operation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Operation {

    /** The operation id <code>String</code>. */
    private String id;

    /** The method to invoke. */
    private Method method;

    /**
     * Create Operation.
     *
     */
    public Operation() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.web.service.Operation#getId()
     *
     */
    public String getId() {
        return id;
    }

    /**
     * @see com.thinkparity.desdemona.web.service.Operation#invoke(com.thinkparity.desdemona.web.service.Service, com.thinkparity.desdemona.web.service.Parameter[])
     *
     */
    public Result invoke(final Service service, final Parameter[] parameters) {
        final ServiceSEI serviceSEI = newInstance(service);
        try {
            return newResult(method.invoke(serviceSEI, newArgs(parameters)));
        } catch (final Exception x) {
            throw new ServiceException(x, "Could not invoke operation {0}:{1}.",
                    service.getId(), id);
        }
    }

    /**
     * Set the operation id.
     * 
     * @param id
     *            An operation id <code>String</code>.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Set the operation method.
     * 
     * @param method
     *            A <code>Method</code>.
     */
    public void setMethod(final Method method) {
        this.method = method;
    }

    /**
     * Create a new argument array from the parameter array.
     * 
     * @param parameters
     *            A <code>Parameter[]</code>.
     * @return An <code>Object[]</code>.
     */
    private Object[] newArgs(final Parameter[] parameters) {
        final Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = parameters[i].getValue();
        }
        return args;
    }

    /**
     * Create a new instance of a service endpoint implementation.
     * 
     * @param service
     *            A <code>Service</code>.
     * @return A <code>ServiceSEI</code>.
     */
    private ServiceSEI newInstance(final Service service) {
        try {
            return service.newSEIInstance();
        } catch (final IllegalAccessException iax) {
            throw new ServiceException(iax, "Could not instantiate operation {0}:{1}.",
                    service.getId(), id);
        } catch (final InstantiationException ix) {
            throw new ServiceException(ix, "Could not instantiate operation {0}:{1}.",
                    service.getId(), id);
        }
    }

    /**
     * Create a new result for the value.
     * 
     * @param value
     *            An <code>Object</code> value.
     * @return A <code>Result</code>.
     */
    private Result newResult(final Object value) {
        final Result result = new Result() {
            public Object getValue() {
                return value;
            }
        };
        return result;
    }
}
