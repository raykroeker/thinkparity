/*
 * Created On:  21-Jun-07 8:39:31 PM
 */
package com.thinkparity.codebase.constraint;

import java.util.List;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Common List Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ListConstraint<T extends Object> extends Constraint<List<T>> {

    /** The maximum list size. */
    private Integer maxSize;

    /** The minimum list size. */
    private Integer minSize;

    /**
     * Create ListConstraint.
     *
     */
    public ListConstraint() {
        super();
    }

    /**
     * Obtain maxSize.
     *
     * @return A Integer.
     */
    public Integer getMaxSize() {
        return maxSize;
    }

    /**
     * Obtain minSize.
     *
     * @return A Integer.
     */
    public Integer getMinSize() {
        return minSize;
    }

    /**
     * Set maxSize.
     *
     * @param maxSize
     *		A Integer.
     */
    public void setMaxSize(final Integer maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Set minSize.
     *
     * @param minSize
     *		A Integer.
     */
    public void setMinSize(final Integer minSize) {
        this.minSize = minSize;
    }

    /**
     * @see com.thinkparity.codebase.constraint.Constraint#validate(java.lang.Object)
     *
     */
    @Override
    public void validate(final List<T> value) {
        super.validate(value);
        if (!isNullable().booleanValue()) {
            if (maxSize.intValue() < value.size()) {
                invalidate(Reason.TOO_LARGE, value);
            }
            if (minSize.intValue() > value.size()) {
                invalidate(Reason.TOO_SMALL, value);
            }
        }
    }
}
