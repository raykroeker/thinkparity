/*
 * Created On: Jul 21, 2006 11:20:31 AM
 */
package com.thinkparity.desdemona.model;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ParityModelException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create ParityModelException. */
    ParityModelException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
