/*
 * Created On: May 19, 2006 4:23:19 PM
 * $Id$
 */
package com.thinkparity.migrator;

/**
 * A migration error definition.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class MigrationError extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create MigrationError. */
    public MigrationError(final String message) { super(message); }

    /** Create MigrationError. */
    public MigrationError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
