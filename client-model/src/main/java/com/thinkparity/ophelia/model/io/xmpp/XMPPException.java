/*
 * Created On: Fri May 12 2006 14:19 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp;

import java.text.MessageFormat;

/**
 * An xmpp io layer error.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPException extends RuntimeException {

    /**
     * Create XMPPException.
     * 
     * @param cause
     *        The cause of the xmpp error.
     */
    public XMPPException(final Object message, final Throwable cause) {
        super(null == message ? null : message.toString(), cause);
    }

    /**
     * Create XMPPException.
     *
     * @param message
     *      The xmpp error message.
     */
    public XMPPException(final String message) {
        super(message);
    }

    /**
     * Create XMPPException.
     * 
     * @param pattern
     *            The xmpp error message pattern <code>String</code>.
     * @param arguments
     *            The xmpp error message arguments <code>Object[]</code>.
     */
    public XMPPException(final String pattern, final Object...arguments) {
        super(MessageFormat.format(pattern, arguments));
    }

    /**
     * Create XMPPException.
     * 
     * @param cause
     *        The cause of the xmpp error.
     */
    public XMPPException(final Throwable cause) { super(cause); }
}
