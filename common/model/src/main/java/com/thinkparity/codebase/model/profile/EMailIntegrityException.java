/*
 * Created On:  6-Sep-07 1:38:04 PM
 */
package com.thinkparity.codebase.model.profile;

import java.text.MessageFormat;

import com.thinkparity.codebase.email.EMail;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EMailIntegrityException extends Exception {

    /** An error message pattern. */
    private static final String PATTERN;

    static {
        PATTERN = "E-mail {0} could not be deleted.";
    }

    /**
     * Create EMailIntegrityException.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    public EMailIntegrityException(final EMail email) {
        super(MessageFormat.format(PATTERN, email));
    }

    /**
     * Create EMailIntegrityException.
     * 
     * @param message
     *            A message <code>String</code>.
     */
    public EMailIntegrityException(final String message) {
        super(message);
    }
}
