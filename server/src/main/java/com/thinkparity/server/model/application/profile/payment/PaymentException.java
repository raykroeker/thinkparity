/*
 * Created On:  5-Sep-07 1:17:37 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentException extends Exception {

    /**
     * Create AuthorizationFailedException.
     *
     */
    public PaymentException() {
        super();
    }

    /**
     * Create AuthorizationFailedException.
     * 
     * @param message
     *            A <code>String</code>.
     */
    public PaymentException(final String message) {
        super(message);
    }

    /**
     * Create AuthorizationFailedException.
     * 
     * @param cause
     *            A <code>Throwable</code>.
     */
    public PaymentException(final Throwable cause) {
        super(cause);
    }
}
