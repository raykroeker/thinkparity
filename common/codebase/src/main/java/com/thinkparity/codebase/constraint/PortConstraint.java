/*
 * Created On:  22-Dec-07 4:53:03 PM
 */
package com.thinkparity.codebase.constraint;

/**
 * <b>Title:</b>thinkParity Common Port Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PortConstraint extends IntegerConstraint {

    /**
     * Create PortConstraint.
     *
     */
    public PortConstraint() {
        super();
        super.setMaxValue(Integer.valueOf(65535));
        super.setMinValue(Integer.valueOf(1));
    }

    /**
     * @see com.thinkparity.codebase.constraint.IntegerConstraint#setMaxValue(java.lang.Integer)
     *
     */
    @Override
    public void setMaxValue(final Integer maxValue) {
        throw new UnsupportedOperationException("Cannot set max value on port constraint.");
    }

    /**
     * @see com.thinkparity.codebase.constraint.IntegerConstraint#setMinValue(java.lang.Integer)
     *
     */
    @Override
    public void setMinValue(final Integer minValue) {
        throw new UnsupportedOperationException("Cannot set min value on port constraint.");
    }
}
