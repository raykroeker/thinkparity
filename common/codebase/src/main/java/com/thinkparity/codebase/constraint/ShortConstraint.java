/*
 * Created On:  18-Apr-07 9:44:54 AM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Codebase Short Constraint<br>
 * <b>Description:</b>A constraint used to verify short data type values.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ShortConstraint extends Constraint<Short> {

    /** A maximum value <code>Short</code>. */
    private Short maxValue;

    /** A minumium value <code>Short</code>. */
    private Short minValue;

    /**
     * Create ShortConstraint.
     *
     */
    public ShortConstraint() {
        super();
    }

    /**
     * Obtain the maximum value.
     * 
     * @return A maximum value <code>Short</code>.
     */
    public Short getMaxValue() {
        return maxValue;
    }

    /**
     * Obtain the miniumum value.
     * 
     * @return A miniumum value <code>Short</code>.
     */
    public Short getMinValue() {
        return minValue;
    }

    /**
     * Set the maximum value.
     * 
     * @param maxValue
     *            A maximum value <code>Short</code>.
     */
    public void setMaxValue(final Short maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Set the minimum value.
     * 
     * @param minValue
     *            A minimum value <code>Short</code>.
     */
    public void setMinValue(final Short minValue) {
        this.minValue = minValue;
    }

    /**
     * @see com.thinkparity.codebase.constraint.Constraint#validate(java.lang.Object)
     *
     */
    @Override
    public void validate(final Short value) {
        super.validate(value);
        if (!isNullable().booleanValue()) {
            if (null != minValue && value.compareTo(minValue) < 0)
                invalidate(Reason.TOO_SMALL, value);
            if (null != maxValue && value.compareTo(maxValue) > 0)
                invalidate(Reason.TOO_LARGE, value);
        }
    }
}
