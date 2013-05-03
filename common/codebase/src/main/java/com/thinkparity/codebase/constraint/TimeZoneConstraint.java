/*
 * Created On:  3-Apr-07 9:36:03 AM
 */
package com.thinkparity.codebase.constraint;

import java.util.TimeZone;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Common TimeZone Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TimeZoneConstraint extends Constraint<String> {

    /**
     * Create TimeZoneConstraint.
     *
     */
    public TimeZoneConstraint() {
        super();
    }

    /**
     * @see com.thinkparity.codebase.constraint.Constraint#validate(java.lang.Object)
     *
     */
    @Override
    public void validate(final String value) {
        super.validate(value);
        if (!isNullable().booleanValue()) {
            final TimeZone timeZone = TimeZone.getTimeZone(value);
            if (!timeZone.getID().equals(value))
                invalidate(Reason.FORMAT, value);
        }
    }
}
