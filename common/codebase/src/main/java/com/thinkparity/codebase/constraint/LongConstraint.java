/*
 * Created On:  18-Apr-07 9:44:54 AM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class LongConstraint extends Constraint<Long> {

    /** A maximum value <code>Long</code>. */
    private Long maxValue;

    /** A minumium value <code>Long</code>.. */
    private Long minValue;

    /**
     * Create LongConstraint.
     *
     */
    public LongConstraint() {
        super();
    }

    /**
     * Obtain the maximum value.
     * 
     * @return A maximum value <code>Long</code>.
     */
    public Long getMaxValue() {
        return maxValue;
    }

    /**
     * Obtain the miniumum value.
     * 
     * @return A miniumum value <code>Long</code>.
     */
    public Long getMinValue() {
        return minValue;
    }

    /**
     * Set the maximum value.
     * 
     * @param maxValue
     *            A maximum value <code>Long</code>.
     */
    public void setMaxValue(final Long maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Set the minimum value.
     * 
     * @param minValue
     *            A minimum value <code>Long</code>.
     */
    public void setMinValue(final Long minValue) {
        this.minValue = minValue;
    }

    /**
     * @see com.thinkparity.codebase.constraint.Constraint#validate(java.lang.Object)
     *
     */
    @Override
    public void validate(final Long value) {
        super.validate(value);
        if (!isNullable().booleanValue()) {
            if (null != minValue && value.compareTo(minValue) < 0)
                invalidate(Reason.TOO_SMALL, value);
            if (null != maxValue && value.compareTo(maxValue) > 0)
                invalidate(Reason.TOO_LARGE, value);
        }
    }

}
