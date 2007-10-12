/*
 * Created On:  14-Aug-07 1:16:04 PM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Codebase Integer Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IntegerConstraint extends Constraint<Integer> implements
        Cloneable {

    /** A maximum value. */
    private Integer maxValue;

    /** A minimum value. */
    private Integer minValue;

    /**
     * Create IntegerConstraint.
     *
     */
    public IntegerConstraint() {
        super();
    }

    /**
     * @see java.lang.Object#clone()
     *
     */
    @Override
    public Object clone() {
        try {
            final IntegerConstraint clone = (IntegerConstraint) super.clone();
            clone.setMaxValue(maxValue);
            clone.setMinValue(minValue);
            clone.setName(getName());
            clone.setNullable(isNullable());
            return clone;
        } catch (final CloneNotSupportedException cnsx) {
            throw new UnsupportedOperationException(cnsx);
        }
    }

    /**
     * Obtain maxValue.
     *
     * @return A Integer.
     */
    public Integer getMaxValue() {
        return maxValue;
    }

    /**
     * Obtain minValue.
     *
     * @return A Integer.
     */
    public Integer getMinValue() {
        return minValue;
    }

    /**
     * Set maxValue.
     *
     * @param maxValue
     *		A Integer.
     */
    public void setMaxValue(final Integer maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Set minValue.
     *
     * @param minValue
     *		A Integer.
     */
    public void setMinValue(final Integer minValue) {
        this.minValue = minValue;
    }

    /**
     * @see com.thinkparity.codebase.constraint.Constraint#validate(java.lang.Object)
     *
     */
    @Override
    public void validate(final Integer value) {
        super.validate(value);
        if (null != minValue && value.compareTo(minValue) < 0)
            invalidate(Reason.TOO_SMALL, value);
        if (null != maxValue && value.compareTo(maxValue) > 0)
            invalidate(Reason.TOO_LARGE, value);
    }
}
