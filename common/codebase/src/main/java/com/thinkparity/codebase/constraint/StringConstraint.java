/*
 * Created On:  3-Apr-07 9:36:03 AM
 */
package com.thinkparity.codebase.constraint;

import java.text.MessageFormat;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Common String Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StringConstraint extends Constraint<String> implements Cloneable {

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
     * @see java.lang.Object#clone()
     *
     */
    @Override
    public Object clone() {
        try {
            final StringConstraint clone = (StringConstraint) super.clone();
            clone.setMaxLength(maxLength);
            clone.setMinLength(minLength);
            clone.setName(getName());
            clone.setNullable(isNullable());
            return clone;
        } catch (final CloneNotSupportedException cnsx) {
            throw new UnsupportedOperationException(cnsx);
        }
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
        validateMaxLength(maxLength);
        this.maxLength = maxLength;
    }

    /**
     * Set minLength.
     *
     * @param minLength
     *		A Integer.
     */
    public void setMinLength(final Integer minLength) {
        validateMinLength(minLength);
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
    
    /**
     * Validate max length.
     * 
     * @param maxLength
     *            An <code>Integer</code>.
     */
    private void validateMaxLength(final Integer maxLength) {
        if (null == maxLength) {
            throw new IllegalArgumentException("Cannot set null max length.");
        } else {
            if (null == minLength) {
                return;
            } else {
                if (minLength.intValue() > maxLength.intValue()) {
                    throw new IllegalArgumentException(MessageFormat.format(
                            "Cannot set max length less than min length.  {0}>{1}",
                            minLength, maxLength));
                }
            }
        }
    }

    /**
     * Validate min length.
     * 
     * @param minLength
     *            An <code>Integer</code>.
     */
    private void validateMinLength(final Integer minLength) {
        if (null == minLength) {
            throw new IllegalArgumentException("Cannot set null min length.");
        } else {
            if (null == maxLength) {
                return;
            } else {
                if (maxLength.intValue() < minLength.intValue()) {
                    throw new IllegalArgumentException(MessageFormat.format(
                            "Cannot set min length greater than max length.  {0}>{1}",
                            minLength, maxLength));
                }
            }
        }
    }
}
