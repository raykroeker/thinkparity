/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.jivesoftware.messenger.SessionManager;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.model.queue.QueueModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.apache.log4j.ServerLoggerFactory;
import com.thinkparity.server.org.dom4j.XmlReader;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

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
	 * Assert that the actual and expected jive id's are equal.
	 * 
	 * @param message
	 *            The message.
	 * @param actualJID
	 *            The actual jive id.
	 * @param expectedJID
	 *            The expected jive id.
	 */
	protected void assertEquals(final String message, final JID actualJID,
			final JID expectedJID) {
		Assert.assertTrue(message, actualJID.equals(expectedJID));
	}

	/**
	 * Create a jabber id for the username.
	 * 
	 * @param username
	 *            The user node (username).
	 * @return The jabber id.
	 */
	protected JID buildJID(final String username) {
		return JIDBuilder.build(username);
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
	 * Route an IQ to a jive user. This will determine whether or not the user
	 * is currently online; and if they are not; it will queue the request.
	 * 
	 * @param jid
	 *            The jive user id.
	 * @param iq
	 *            The iq.
	 */
	protected void send(final JID jid, final IQ iq)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("route(JID,IQ)");
		logger.debug(jid);
		logger.debug(iq);
		if(isOnline(jid)) {
			logger.info("isOnline(jid)");
			getSessionManager().getSession(jid).process(iq);
		}
		else { enqueue(jid, iq); }
	}

	/**
	 * Save the iq in the parity offline queue.
	 * 
	 * @param jid
	 *            The jid.
	 * @param iq
	 *            The iq packet.
	 */
	private void enqueue(final JID jid, final IQ iq)
			throws ParityServerModelException {
		final QueueModel queueModel = QueueModel.getModel(session);
		queueModel.enqueue(jid, iq);
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
