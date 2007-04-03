/*
 * Created On:  3-Apr-07 9:36:03 AM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;

/**
 * <b>Title:</b>thinkParity Common String Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class EMailConstraint extends StringConstraint {

    /**
     * Create StringConstraint.
     *
     */
    public EMailConstraint() {
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
            try {
                EMailBuilder.parse(value);
            } catch (final EMailFormatException emfx) {
                invalidate(Reason.FORMAT, value);
            }
        }
    }
}
