/*
 * Created On: Fri May 12 2006 14:19 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp;

/**
 * An xmpp io layer error.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPException extends RuntimeException {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

    /**
     * Create XMPPException.
     * 
     * @param cause
     *        The cause of the xmpp error.
     */
    public XMPPException(final Throwable cause) { super(cause); }

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
	public XMPPException(final String message) { super(message); }
}
