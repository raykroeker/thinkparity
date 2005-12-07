/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.jivesoftware.messenger.PacketRouter;
import org.jivesoftware.messenger.SessionManager;
import org.jivesoftware.messenger.XMPPServer;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.dom4j.XmlReader;
import com.thinkparity.server.log4j.ServerLoggerFactory;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Handle to a parity server logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Handle to the user's session.
	 */
	protected final Session session;

	/**
	 * Create an AbstractModelImpl.
	 */
	protected AbstractModelImpl(final Session session) {
		super();
		this.session = session;
	}

	/**
	 * Create a jabber id for the username.
	 * 
	 * @param username
	 *            The user node (username).
	 * @return The jabber id.
	 */
	protected JID createJID(final String username) {
		return getXMPPServer().createJID(
				username, ParityServerConstants.CLIENT_RESOURCE);
	}

	/**
	 * Determine whether or not the user represented by the jabber id is
	 * currently online.
	 * 
	 * @param jid
	 *            The jabber id to check.
	 * @return True; if the user is online; false otherwise.
	 */
	protected Boolean isOnline(final JID jid) {
		final SessionManager sessionManager = getSessionManager();
		if(0 < sessionManager.getSessionCount(jid.getNode())) {
			return Boolean.TRUE;
		}
		else { return Boolean.FALSE; }
	}

	/**
	 * Read the xml into a dom4j document.
	 * 
	 * @param xml
	 *            The xml.
	 * @return The dom4j document.
	 * @throws DocumentException
	 * @see XmlReader#read(String)
	 */
	protected Document read(final String xml) throws DocumentException {
		return XmlReader.read(xml);
	}

	/**
	 * Route an IQ.
	 * 
	 * @param iq
	 *            The iq.
	 */
	protected void route(final IQ iq) { getPacketRouter().route(iq); }

	/**
	 * Obtain a handle to the xmpp server's packet router.
	 * 
	 * @return The xmpp server's packet router.
	 */
	private PacketRouter getPacketRouter() {
		return getXMPPServer().getPacketRouter();
	}

	/**
	 * Obtain a handle to the xmpp server's session manager.
	 * @return The xmpp servers's session manager.
	 */
	private SessionManager getSessionManager() {
		return getXMPPServer().getSessionManager();
	}

	/**
	 * Obtain a handle to the underlying xmpp server.
	 * 
	 * @return The xmpp server.
	 */
	private XMPPServer getXMPPServer() { return XMPPServer.getInstance(); }
}
