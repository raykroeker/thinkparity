/*
 * Created On:  22-Dec-07 4:22:27 PM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Common Host Name Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DomainNameConstraint extends StringConstraint {

    /** Legal host name characters. */
    private static final char[] legal;

    static {
        legal = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '-', '.' };
    }

    /**
     * Create DomainNameConstraint.
     *
     */
    public DomainNameConstraint() {
        super();
        super.setMaxLength(255);
        super.setMinLength(5);
    }

    /**
     * @see com.thinkparity.codebase.constraint.StringConstraint#setMaxLength(java.lang.Integer)
     *
     */
    @Override
    public void setMaxLength(final Integer maxLength) {
        throw new UnsupportedOperationException("Cannot set max length on domain name constraint.");
    }

    /**
     * @see com.thinkparity.codebase.constraint.StringConstraint#setMinLength(java.lang.Integer)
     *
     */
    @Override
    public void setMinLength(final Integer minLength) {
        throw new UnsupportedOperationException("Cannot set min length on domain name constraint.");
    }

    /**
     * @see com.thinkparity.codebase.constraint.StringConstraint#validate(java.lang.String)
     *
     */
    @Override
    public void validate(final String value) {
        super.validate(value);
        if (null == value) {
            return;
        } else {
            boolean illegal;
            for (final char v : value.toCharArray()) {
                illegal = true;
                for (final char l : legal) {
                    if (v == l) {
                        illegal = false;
                        break;
                    }
                }
                if (true == illegal) {
                    invalidate(Reason.ILLEGAL, value);
                }
            }
        }
    }
}
