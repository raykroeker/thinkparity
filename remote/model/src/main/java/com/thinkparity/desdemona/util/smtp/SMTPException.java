/*
 * Created On: Jul 21, 2006 11:04:50 AM
 */
package com.thinkparity.desdemona.util.smtp;


/**
 * <b>Title:</b>thinkParity Desdemona Util SMTP Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SMTPException extends RuntimeException {

    /**
     * Create SMTPException.
     * 
     * @param cause
     *            The cause <code>Throwable</code> of the error.
     */
    SMTPException(final Throwable cause) {
        super(cause);
    }
}