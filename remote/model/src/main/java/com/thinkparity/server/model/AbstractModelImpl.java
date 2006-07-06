/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model;

import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.messenger.SessionManager;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.LoggerFactory;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.contact.ContactModel;
import com.thinkparity.server.model.document.DocumentModel;
import com.thinkparity.server.model.queue.QueueModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.session.SessionModel;
import com.thinkparity.server.model.user.UserModel;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

    /**
     * Obtain a log4j model id.
     * 
     * @param model
     *            The model.
     * @return A model id.
     */
    protected static StringBuffer getModelId(final String model) {
        return new StringBuffer("[RMODEL]").append(" ").append(model);
    }

	/**
	 * Handle to a parity server logger.
	 */
	protected final Logger logger;

	/**
	 * Handle to the user's session.
	 */
	protected final Session session;

	/**
	 * Create an AbstractModelImpl.
	 */
	protected AbstractModelImpl(final Session session) {
		super();
        this.logger = LoggerFactory.getLogger(getClass());
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
	 * Assert that the session user is the artifact key holder.
	 * 
	 * @param artifact
	 *            The artifact.
	 * @throws NotTrueAssertion
	 *             If the user is not the key holder.
	 */
	protected void assertIsKeyHolder(final Artifact artifact) {
		Assert.assertTrue("User is not the key holder.",
				artifact.getArtifactKeyHolder().equals(
						session.getJID().getNode()));
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
     * Obtain the parity artifact interface.
     * 
     * @return The parity artifact interface.
     */
	protected ArtifactModel getArtifactModel() {
		final ArtifactModel artifactModel = ArtifactModel.getModel(session);
		return artifactModel;
	}

    /**
     * Obtain the parity contact interface.
     * 
     * @return The parity contact interface.
     */
	protected ContactModel getContactModel() {
		return ContactModel.getModel(session);
	}

	/**
     * Obtain the parity document interface.
     * 
     * @return The parity document interface.
     */
    protected DocumentModel getDocumentModel() {
        return DocumentModel.getModel(session);
    }

	/**
	 * Obtain the parity session interface.
	 * 
	 * @return The parity session interface.
	 */
	protected SessionModel getSessionModel() {
		final SessionModel sModel = SessionModel.getModel(session);
		return sModel;
	}

	/**
     * Obtain the parity user interface.
     * 
     * @return The parity user interface.
     */
	protected UserModel getUserModel() {
		final UserModel uModel = UserModel.getModel(session);
		return uModel;
	}

	/**
     * Determine if the user account is still active.
     * 
     * @param jabberId
     *            The user's jabber id.
     * @return True if the account is active; false otherwise.
     * TODO Finish isActive implementation.
     */
    protected Boolean isActive(final JabberId jabberId) { return Boolean.TRUE; }

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

	protected Boolean isSessionUserKeyHolder(final UUID artifactUniqueId)
			throws ParityServerModelException {
		final ArtifactModel aModel = getArtifactModel();
		return aModel.get(artifactUniqueId).getArtifactKeyHolder().equals(
				session.getJID().getNode());
	}

	/**
	 * Route an IQ to a jive user. This will determine whether or not the user
	 * is currently online; and if they are not; it will queue the request.
	 * 
	 * @param jabberId
	 *            The jabber id.
	 * @param iq
	 *            The iq.
	 */
	protected void send(final JabberId jabberId, final IQ iq)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("send(JabberId,IQ)");
		logger.debug(jabberId);
		send(jabberId.getJID(), iq);
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
		logger.info("send(JID,IQ)");
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
