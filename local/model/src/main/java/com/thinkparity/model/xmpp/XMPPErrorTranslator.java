/*
 * 19-Oct-2005
 */
package com.thinkparity.model.xmpp;

import org.jivesoftware.smack.XMPPException;

import com.thinkparity.model.smack.SmackException;

/**
 * XMPP interface error utilities. Translation of error types into interface
 * error types are done here.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class XMPPErrorTranslator {

	/**
	 * Singleton instance.
	 * @see XMPPErrorTranslator#singletonLock
	 */
	private static final XMPPErrorTranslator singleton;

	/**
	 * Singleton synchronization lock.
	 * @see XMPPErrorTranslator#singleton
	 */
	private static final Object singletonLock;

	static {
		singleton = new XMPPErrorTranslator();
		singletonLock = new Object();
	}

	/**
	 * Create a new smack interface error based upon a java interrupted error.
	 * 
	 * @param ix
	 *            The java interrupted error.
	 * @return The smack interface error.
	 */
	static SmackException translate(final InterruptedException ix) {
		synchronized(singletonLock) { return singleton.translateImpl(ix); }
	}

	/**
	 * Create a new smack interface error based upon an xmppp error.
	 * 
	 * @param xmppx
	 *            The xmpp error.
	 * @return The smack interface error.
	 */
	static SmackException translate(final XMPPException xmppx) {
		synchronized(singletonLock) { return singleton.translateImpl(xmppx); }
	}

	/**
	 * Create a XMPPErrorTranslator [Singleton]
	 */
	private XMPPErrorTranslator() { super(); }

	/**
	 * Create a new smack interface error based upon a java interrupted error.
	 * 
	 * @param ix
	 *            The java interrupted error.
	 * @return The smack interface error.
	 */
	private SmackException translateImpl(final InterruptedException ix) {
		return new SmackException(ix);
	}

	/**
	 * Create a new smack interface error based upon an xmppp error.
	 * 
	 * @param xmppx
	 *            The xmpp error.
	 * @return The smack interface error.
	 */
	private SmackException translateImpl(final XMPPException xmppx) {
		return new SmackException(xmppx);
	}
}
