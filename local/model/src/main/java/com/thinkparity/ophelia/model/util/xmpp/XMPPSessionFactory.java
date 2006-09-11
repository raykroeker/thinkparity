/*
 * Feb 6, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp;



/**
 * XMPPSessionFactory
 * Singleton factory for the XMPPSession.  Only 1 session can be created per
 * runtime.  The factory is also responsible for registering the extensions for
 * the client.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public final class XMPPSessionFactory {

	private static final XMPPSessionFactory singleton;

	private static final Object singletonLock;

	/**
	 * Register all of the packet extensions.
	 * 
	 */
	static {
		singleton = new XMPPSessionFactory();
		singletonLock = new Object();
	}

	/**
	 * Obtain a handle to the session.
	 * @return <code>org.kcs.projectmanager.smack.SmackSession</code>
	 */
	public static XMPPSession createSession() {
		synchronized(singletonLock) { return singleton.doCreateSession(); }
	}

	/**
	 * Create a XMPPSessionFactory [Singleton, Factory]
	 * 
	 */
	private XMPPSessionFactory() { super(); }

	/**
	 * The session.
	 * 
	 */
	private XMPPSession session;

	/**
	 * Create an xmpp session interface.
	 * 
	 * @return The xmpp session interface.
	 */
	private XMPPSession doCreateSession() {
		if(null == session) { session = new XMPPSessionImpl(); }
		return session;
	}
}
