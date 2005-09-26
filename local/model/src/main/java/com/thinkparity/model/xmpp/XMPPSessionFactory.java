/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import com.thinkparity.model.smackx.provider.XManager;


/**
 * XMPPSessionFactory
 * Singleton factory for the XMPPSession.  Only 1 session can be created per
 * runtime.  The factory is also responsible for registering the extensions for
 * the client.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public final class XMPPSessionFactory {

	/**
	 * Handle to the session.
	 */
	private static XMPPSessionImpl xmppSessionImpl;

	/**
	 * Register all of the packet extensions.
	 */
	static { XManager.register(); }

	/**
	 * Obtain a handle to the session.
	 * @return <code>org.kcs.projectmanager.smack.SmackSession</code>
	 */
	public static XMPPSession getSession() {
		createSession();
		return xmppSessionImpl;
	}

	/**
	 * Determine whether or not a session has been created, and if not create
	 * one and set the local variable.
	 */
	private static void createSession() {
		// create a session
		if(null == xmppSessionImpl)
			xmppSessionImpl = new XMPPSessionImpl();
	}

	/**
	 * Create a XMPPSessionFactory [Singleton]
	 */
	private XMPPSessionFactory() { super(); }
}
