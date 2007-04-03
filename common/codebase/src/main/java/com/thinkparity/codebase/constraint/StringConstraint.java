/*
 * Created On:  3-Apr-07 9:36:03 AM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Common String Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StringConstraint extends Constraint<String> {

    /** The maximum string length <code>Integer</code>. */
    private Integer maxLength;

    /** The minimum string length <code>Integer</code>. */
    private Integer minLength;

    /**
     * Create StringConstraint.
     *
     */
    public StringConstraint() {
        super();
    }

    /**
     * Obtain maxLength.
     *
     * @return A Integer.
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Obtain minLength.
     *
     * @return A Integer.
     */
    public Integer getMinLength() {
        return minLength;
    }

    /**
     * Set maxLength.
     *
     * @param maxLength
     *		A Integer.
     */
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Set minLength.
     *
     * @param minLength
     *		A Integer.
     */
    public void setMinLength(final Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * @see com.thinkparity.codebase.constraint.Constraint#validate(java.lang.Object)
     *
     */
    @Override
    public void validate(final String value) {
        super.validate(value);
        if (!isNullable().booleanValue()) {
            if (maxLength.intValue() < value.length())
                invalidate(Reason.TOO_LONG, value);
            if (minLength.intValue() > value.length())
                invalidate(Reason.TOO_SHORT, value);
        }
    }
}
