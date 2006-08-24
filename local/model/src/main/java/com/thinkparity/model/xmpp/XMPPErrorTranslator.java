/*
 * 19-Oct-2005
 */
package com.thinkparity.model.xmpp;

import java.io.IOException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;

import com.thinkparity.model.smack.SmackException;

/**
 * XMPP interface error utilities. Translation of error types into interface
 * error types are done here.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class XMPPErrorTranslator {

	/** Singleton instance. */
	private static final XMPPErrorTranslator SINGLETON;

	static { SINGLETON = new XMPPErrorTranslator(); }

    /**
	 * Create a new smack interface error based upon a java interrupted error.
	 * 
	 * @param ix
	 *            The java interrupted error.
	 * @return The smack interface error.
	 */
	static SmackException translate(final InterruptedException ix) {
		return SINGLETON.doTranslate(ix);
	}

	static SmackException translate(final IOException iox) {
        return SINGLETON.doTranslate(iox);
    }

	static SmackException translate(final XMPPError xmppError) {
        return SINGLETON.doTranslate(xmppError);
	}

    /**
	 * Create a new smack interface error based upon an xmppp error.
	 * 
	 * @param xmppx
	 *            The xmpp error.
	 * @return The smack interface error.
	 */
	static SmackException translate(final XMPPException xmppx) {
        return SINGLETON.doTranslate(xmppx);
	}

	/**
     * Translate an error into an xmpp unechecked error.
     * 
     * @param xmppCore
     *            The xmpp core.
     * @param errorId
     *            An error id.
     * @param t
     *            An error.
     * @return An unchecked xmpp exception.
     */
    static XMPPUncheckedException translateUnchecked(final XMPPCore xmppCore,
            final Object errorId, final Throwable t) {
        return SINGLETON.doTranslateUnchecked(xmppCore, errorId, t);
    }

	/**
	 * Create a XMPPErrorTranslator [Singleton]
	 */
	private XMPPErrorTranslator() { super(); }

	/**
	 * Create a new smack interface error based upon an xmppp error.
	 * 
	 * @param xmppx
	 *            The xmpp error.
	 * @return The smack interface error.
	 */
	private SmackException doTranslate(final Exception x) {
		return new SmackException(x);
	}

    private SmackException doTranslate(final XMPPError xmppError) {
        return new SmackException(xmppError);
    }

    private XMPPUncheckedException doTranslateUnchecked(final XMPPCore xmppCore, final Object errorId, final Throwable t) {
        return new XMPPUncheckedException(errorId.toString(), t);
    }
}
