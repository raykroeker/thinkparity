/*
 * Created On:  3-Apr-07 12:13:30 PM
 */
package com.thinkparity.codebase.constraint;

/**
 * <b>Title:</b>thinkParity Common Illegal Value Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IllegalValueException extends IllegalArgumentException {

    /** The constraint name <code>String</code>. */
    private final String name;

    /** A <code>Reason</code>. */
    private final Reason reason;

    /** The value <code>Object</code>. */
    private final Object value;

    /**
     * Create IllegalValueException.
     * 
     * @param reason
     *            A <code>Reason</code>.
     */
    IllegalValueException(final String name, final Object value,
            final Reason reason) {
        super();
        this.name = name;
        this.value = value;
        this.reason = reason;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain the reason for the error.
     * 
     * @return A <code>Reason</code>.
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * Obtain value.
     *
     * @return A Object.
     */
    public Object getValue() {
        return value;
    }

    /**
     * <b>Title:</b>Illegal Value Reason<br>
     * <b>Description:</b>An enumeration of the cause of the exception.<br>
     */
    public enum Reason { FORMAT, NULL, TOO_LARGE, TOO_LONG, TOO_SHORT, TOO_SMALL }
}
