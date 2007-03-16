/*
 * Created On:  15-Mar-07 11:42:18 AM
 */
package com.thinkparity.codebase.model.migrator;

import java.lang.reflect.Method;

/**
 * <b>Title:</b>thinkParity Error<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Error {

    /** The method arguments <code>Object[]</code>. */
    private Object[] arguments;

    /** The cause <code>Throwable</code>. */
    private Throwable cause;

    /** The <code>Method</code>. */
    private Method method;

    /**
     * Create Error.
     *
     */
    public Error() {
        super();
    }

    /**
     * Obtain arguments.
     *
     * @return A Object[].
     */
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * Obtain cause.
     *
     * @return A Throwable.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Obtain method.
     *
     * @return A Method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Set arguments.
     *
     * @param arguments
     *		A Object[].
     */
    public void setArguments(final Object[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Set cause.
     *
     * @param cause
     *		A Throwable.
     */
    public void setCause(final Throwable cause) {
        this.cause = cause;
    }

    /**
     * Set method.
     *
     * @param method
     *		A Method.
     */
    public void setMethod(final Method method) {
        this.method = method;
    }
}
