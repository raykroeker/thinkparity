/*
 * Created On: Jul 21, 2006 11:04:50 AM
 */
package com.thinkparity.model.util.smtp;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class SMTPException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Translate a messaging exception into an smtp exception.
     * 
     * @param mx
     *            A messaging exception.
     * @return An smtp exception.
     */
    static SMTPException translate(final MessagingException mx) {
        return new SMTPException(mx);
    }

    /**
     * Translate an unsupported encoding exception into an smtp exception.
     * 
     * @param uex
     *            An unsupported encoding exception.
     * @return An smtp exception.
     */
    static SMTPException translate(final UnsupportedEncodingException uex) {
        return new SMTPException(uex);
    }

    /** Create SMTPException. */
    private SMTPException(final Throwable cause) {
        super(cause);
    }
}
