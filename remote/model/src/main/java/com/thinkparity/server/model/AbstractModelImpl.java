/*
 * Created On: Nov 28, 2005
 */
package com.thinkparity.server.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import org.jivesoftware.messenger.SessionManager;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.ParityServerConstants.Logging;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.model.contact.ContactModel;
import com.thinkparity.server.model.document.DocumentModel;
import com.thinkparity.server.model.queue.QueueModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.session.SessionModel;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.model.user.UserModel;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

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
        this.logger = Logger.getLogger(getClass());
		this.session = session;
	}


    /**
     * Add a to email recipient to a mime message.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param email
     *            An <code>EMail</code>.
     * @throws MessagingException
     */
    protected void addRecipient(final MimeMessage mimeMessage, final EMail email)
            throws MessagingException {
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(email.toString(), Boolean.TRUE));
    }

	/**
     * Assert that the actual and expected jabber id's are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected jabber id.
     * @param actual
     *            The actual jabber id.
     */
    protected void assertEquals(final Object assertion,
            final JabberId expected, final JabberId actual) {
        Assert.assertTrue(assertion, expected.equals(actual));
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
     * Assert that the user id matched that of the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @see #isAuthenticatedUser(JabberId)
     */
    protected void assertIsAuthenticatedUser(final JabberId userId) {
        Assert.assertTrue("USER DOES NOT MATCH AUTHENTICATED USER",
                isAuthenticatedUser(userId));
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
     * Assert that the thinkParity system is the current key holder.
     * 
     * @param assertion
     *            The assertion.
     * @param uniqueId
     *            The artifact unique id.
     */
    protected void assertSystemIsKeyHolder(final Object assertion,
            final UUID uniqueId) throws ParityServerModelException {
        Assert.assertTrue(assertion, isSystemKeyHolder(uniqueId));
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
     * Create an iq writer for a query.
     * 
     * @param queryName
     *            A query name.
     * @return An iq writer.
     */
    protected IQWriter createIQWriter(final String queryName) {
        final IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement(Xml.NAME, new StringBuffer(Xml.NAMESPACE)
                .append(":").append(queryName).toString());
        return new IQWriter(iq);
    }

    /**
     * @deprecated Use {@link #logVariable(String, Object)} instead.
     * 
     */
    protected final void debugVariable(final String name, final Object value) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("{0}:{1}",
                    name,
                    Log4JHelper.render(logger, value)));
        }
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
     * Obtain an error id.
     * 
     * @return An error id.
     */
    protected final Object getErrorId(final Throwable t) {
        return MessageFormat.format("[{0}] [{1}] [{2}] - [{3}]",
                    Logging.MODEL_LOG_ID,
                    StackUtil.getFrameClassName(2).toUpperCase(),
                    StackUtil.getFrameMethodName(2).toUpperCase(),
                    t.getMessage());
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
     * Determine if the user id is the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches the currently authenticated user.
     */
    protected Boolean isAuthenticatedUser(final JabberId userId) {
        return session.getJabberId().equals(userId);
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

	protected Boolean isSessionUserKeyHolder(final UUID artifactUniqueId)
			throws ParityServerModelException {
		final ArtifactModel aModel = getArtifactModel();
		return aModel.get(artifactUniqueId).getArtifactKeyHolder().equals(
				session.getJID().getNode());
	}

    /** Log an api id. */
    protected final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}]",
                    Logging.MODEL_LOG_ID,
                    StackUtil.getCallerClassName().toUpperCase(),
                    StackUtil.getCallerMethodName().toUpperCase()));
        }
    }

	/**
     * Log an api id with a message.
     * 
     * @param message
     *            A message.
     */
    protected final void logApiId(final Object message) {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}] [{3}]",
                    Logging.MODEL_LOG_ID,
                    StackUtil.getCallerClassName().toUpperCase(),
                    StackUtil.getCallerMethodName().toUpperCase(),
                    message));
        }
    }

	/**
     * Log a named variable. Note that the logging renderer will be used only
     * for the value.
     * 
     * @param name
     *            A variable name.
     * @param value
     *            A variable.
     * @return The value.
     */
    protected final <T> T logVariable(final String name, final T value) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("{0}:{1}",
                    name,
                    Log4JHelper.render(logger, value)));
        }
        return value;
    }

	/**
     * Send a notification to an artifact team. Note that the user this session
     * belongs to will *NOT* be notified. This eases data integrity concerns on
     * the network; for example if a team member is added and the person adding
     * the team member is notified; they need to check for existing pending team
     * member row in the db before inserting it. A bit of a hack.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param notification
     *            The notification internet query.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    protected void notifyTeam(final UUID uniqueId, final IQ notification)
            throws ParityServerModelException, UnauthorizedException {
        final List<JabberId> team = readTeam(uniqueId);
        for(final JabberId teamMember : team) {
            if(!teamMember.equals(session.getJabberId())) {
                notification.setTo(teamMember.getJID());
                send(teamMember, notification);
            }
        }
    }

	/**
     * Read the key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return The key holder jabber id.
     * @throws ParityServerModelException
     */
    protected JabberId readKeyHolder(final UUID uniqueId)
            throws ParityServerModelException {
        final ArtifactModel aModel = getArtifactModel();
        return JabberIdBuilder.parseJID(buildJID(aModel.get(uniqueId).getArtifactKeyHolder()));
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
	    logApiId();
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
        logApiId();
		logger.debug(jid);
		logger.debug(iq);
		if(isOnline(jid)) { getSessionManager().getSession(jid).process(iq); }
		else { enqueue(jid, iq); }
	}

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param message
     *            An error message.
     * @param t
     *            An error.
     */
    protected ParityModelException translateError(final Throwable t) {
        if(ParityModelException.class.isAssignableFrom(t.getClass())) {
            return (ParityModelException) t;
        }
        else {
            final Object errorId = getErrorId(t);
            logger.error(errorId, t);
            return ParityErrorTranslator.translateUnchecked(session, errorId, t);
        }
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


    /**
     * Determine whether or not the system account is the key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return True if the system account is the key holder; false otherwise.
     * @throws ParityServerModelException
     */
    private Boolean isSystemKeyHolder(final UUID uniqueId)
            throws ParityServerModelException {
        return readKeyHolder(uniqueId).equals(User.THINK_PARITY.getId());
    }


    /**
     * Read the team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A list of jabber ids.
     * @throws ParityServerModelException
     */
    private List<JabberId> readTeam(final UUID uniqueId)
            throws ParityServerModelException {
        final List<ArtifactSubscription> subscription = getArtifactModel().getSubscription(uniqueId);
        final List<JabberId> team = new ArrayList<JabberId>(subscription.size());
        for(final ArtifactSubscription s : subscription) {
            team.add(s.getJabberId());
        }
        return team;
    }
}
