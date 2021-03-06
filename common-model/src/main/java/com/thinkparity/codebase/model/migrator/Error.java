/*
 * Created On:  15-Mar-07 11:42:18 AM
 */
package com.thinkparity.codebase.model.migrator;

import java.util.Calendar;

/**
 * <b>Title:</b>thinkParity Error<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Error {

    /** The method arguments <code>String[]</code>. */
    private String[] arguments;

    /** The stack trace <code>String</code>. */
    private String stack;

    /** An error id <code>String</code>. */
    private transient Long id;

    /** The method signature <code>String</code>. */
    private String method;

    /** The occured on <code>Calendar</code>. */
    private Calendar occuredOn;

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
    public String getStack() {
        return stack;
    }

    /**
     * Obtain id.
     *
     * @return A Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain method.
     *
     * @return A Method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Obtain occuredOn.
     *
     * @return A Calendar.
     */
    public Calendar getOccuredOn() {
        return occuredOn;
    }

    /**
     * Set arguments.
     *
     * @param arguments
     *		A Object[].
     */
    public void setArguments(final String[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Set cause.
     *
     * @param cause
     *		A Throwable.
     */
    public void setStack(final String stack) {
        this.stack = stack;
    }

    /**
     * Set id.
     *
     * @param id
     *		A Long.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Set method.
     *
     * @param method
     *		A Method.
     */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * Set occuredOn.
     *
     * @param occuredOn
     *		A Calendar.
     */
    public void setOccuredOn(final Calendar occuredOn) {
        this.occuredOn = occuredOn;
    }
}
