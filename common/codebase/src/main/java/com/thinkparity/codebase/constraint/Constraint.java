/*
 * Created On:  3-Apr-07 9:34:33 AM
 */
package com.thinkparity.codebase.constraint;

/**
 * <b>Title:</b>thinkParity Common Abstract Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            The object type to validate.
 */
public abstract class Constraint<T extends Object> {

    /** The constraint name <code>String</code>. */
    private String name;

    /** A flag indicating whether or not the string can be null. */
    private Boolean nullable;

    /**
     * Create Constraint.
     *
     */
    public Constraint() {
        super();
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
     * Obtain nullable.
     *
     * @return A Boolean.
     */
    public Boolean isNullable() {
        return nullable;
    }

    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set nullable.
     *
     * @param nullable
     *      A Boolean.
     */
    public void setNullable(final Boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Validate the object.
     * 
     * @param o
     *            The object to validate.
     */
    public void validate(final T o) {
        if (!nullable.booleanValue())
            if (null == o)
                throw new IllegalArgumentException();
    }
}
