/*
 * Created On:  3-Apr-07 9:36:03 AM
 */
package com.thinkparity.codebase.constraint;

import java.util.Locale;

import com.thinkparity.codebase.LocaleUtil;
import com.thinkparity.codebase.constraint.IllegalValueException.Reason;

/**
 * <b>Title:</b>thinkParity Common Language Constraint<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class LanguageConstraint extends Constraint<String> {

    /**
     * Create LanguageConstraint.
     *
     */
    public LanguageConstraint() {
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
            final Locale[] locales = LocaleUtil.getInstance().getAvailableLocales();
            boolean found = false;
            for (final Locale locale : locales) {
                if (locale.getISO3Language().equals(value)) {
                    found = true;
                    break;
                }
            }
            if (!found)
                invalidate(Reason.FORMAT, value);
        }
    }
}
