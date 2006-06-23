/*
 * Oct 9, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImplHelper;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.XMPPSession;
import com.thinkparity.model.xmpp.XMPPSessionFactory;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.events.XMPPContactListener;
import com.thinkparity.model.xmpp.events.XMPPDocumentListener;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * The session model xmpp helper is used as an intermediary between the parity
 * session model and the xmpp session implementation. It handles all of the
 * events generated by the xmpp implementation and passes them on to the session
 * for further analysis.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
class SessionModelXMPPHelper extends AbstractModelImplHelper {

	/**
	 * The xmpp artifact event listener.
	 * 
	 */
	private final XMPPArtifactListener xmppArtifactListener;

    /**
     * The xmpp document event listener.
     * 
     */
    private final XMPPDocumentListener xmppDocumentListener;

	/**
	 * XMPP Extension listener.
	 */
	private final XMPPExtensionListener xmppExtensionListener;

	/**
	 * XMPP Presence listener.
	 */
	private final XMPPContactListener xmppPresenceListener;

	/**
	 * XMPP session.
	 */
	private final XMPPSession xmppSession;

	/**
	 * XMPP Session listener.
	 */
	private final XMPPSessionListener xmppSessionListener;

	/**
	 * Create a SessionModelXMPPHelper
	 */
	SessionModelXMPPHelper() {
		super();
		this.xmppSession = XMPPSessionFactory.createSession();
		this.xmppArtifactListener = new XMPPArtifactListener() {
			public void confirmReceipt(final UUID uniqueId,
                    final Long versionId, final JabberId receivedFrom) {
                handleConfirmationReceipt(uniqueId, versionId, receivedFrom);
            }
			public void teamMemberAdded(final UUID artifactUniqueId,
					final Contact teamMember) {
				handleTeamMemberAdded(artifactUniqueId, teamMember);
			}
            public void teamMemberRemoved(final UUID artifactUniqueId,
					final Contact teamMember) {
				handleTeamMemberRemoved(artifactUniqueId, teamMember);
			}
		};
        this.xmppDocumentListener = new XMPPDocumentListener() {
            public void documentReactivated(final JabberId reactivatedBy,
                    final List<JabberId> team, final UUID uniqueId,
                    final Long versionId, final String name,
                    final byte[] content) {
                handleDocumentReactivated(reactivatedBy, team, uniqueId,
                        versionId, name, content);
            }
            public void documentReceived(final JabberId receivedFrom,
                    final UUID uniqueId, final Long versionId,
                    final String name, final byte[] content) {
                handleDocumentReceived(receivedFrom, uniqueId, versionId, name, content);
            }
        };
		this.xmppExtensionListener = new XMPPExtensionListener() {
			public void artifactClosed(final UUID artifactUniqueId,
					final JabberId artifactClosedBy) {
				handleArtifactClosed(artifactUniqueId, artifactClosedBy);
			}
			public void keyRequestAccepted(final UUID artifactUniqueId,
					final JabberId acceptedBy) {
				handleKeyRequestAccepted(artifactUniqueId, acceptedBy);
			}
			public void keyRequestDenied(final UUID artifactUniqueId,
					final JabberId deniedBy) {
				handleKeyRequestDenied(artifactUniqueId, deniedBy);
			}
			public void keyRequested(final UUID artifactUniqueId, final JabberId requestedBy) {
				handleKeyRequested(artifactUniqueId, requestedBy);
			}
		};
		this.xmppPresenceListener = new XMPPContactListener() {
			public void invitationAccepted(final JabberId acceptedBy) {
				handleInvitationAccepted(acceptedBy);
			}
			public void invitationDeclined(final JabberId declinedBy) {
				handleInvitationDeclined(declinedBy);
			}
			public void invitationExtended(final JabberId invitedBy) {
				handleInvitationExtended(invitedBy);
			}
		};
		this.xmppSessionListener = new XMPPSessionListener() {
			public void sessionEstablished() { handleSessionEstablished(); }
			public void sessionTerminated() { handleSessionTerminated(); }
			public void sessionTerminated(final Exception x) {
				handleSessionTerminated(x);
			}
		};

		xmppSession.addListener(xmppArtifactListener);
        xmppSession.addListener(xmppDocumentListener);
		xmppSession.addListener(xmppExtensionListener);
		xmppSession.addListener(xmppPresenceListener);
		xmppSession.addListener(xmppSessionListener);
	}

	/**
	 * Accept an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws SmackException
	 */
	void acceptInvitation(final JabberId jabberId) throws SmackException {
		xmppSession.acceptInvitation(jabberId);
	}

    /**
     * Send an artifact received confirmation receipt.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     */
    void confirmArtifactReceipt(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId)
            throws SmackException {
       xmppSession.confirmArtifactReceipt(receivedFrom, uniqueId, versionId);
    }

    /**
	 * Decline an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws SmackException
	 */
	void declineInvitation(final JabberId jabberId) throws SmackException {
		xmppSession.declineInvitation(jabberId);
	}

	/**
	 * Obtain the artifact key holder.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @return The artifact key holder.
	 */
	User getArtifactKeyHolder(final UUID artifactUniqueId)
			throws SmackException {
		return xmppSession.readArtifactKeyHolder(artifactUniqueId);
	}

	/**
	 * Obtain the user for the current session.
	 * 
	 * @return The user for the current session.
	 */
	User getUser() throws SmackException { return xmppSession.readCurrentUser(); }

	/**
	 * Add a user to the roster. This will send a presence visibility request to
	 * the user.
	 * 
	 * @param jabberId
	 *            The jabber id of the contact to invite.
	 * @throws SmackException
	 */
	void inviteContact(final JabberId jabberId) throws SmackException {
		xmppSession.sendInvitation(jabberId);
	}

	/**
	 * Determine if the user is logged in.
	 * 
	 * @return True if the user is logged in false, otherwise.
	 */
	Boolean isLoggedIn() { return xmppSession.isLoggedIn(); }

	/**
	 * Establish a new xmpp session for the user.
	 * 
	 * @param host
	 *            The server host.
	 * @param port
	 *            The server port.
	 * @param username
	 *            The login name.
	 * @param password
	 *            The login password.
	 * @throws SmackException
	 */
	void login(final String host, final Integer port, final String username,
			final String password) throws SmackException {
		xmppSession.login(host, port, username, password);
	}

	/**
	 * Terminate the existing session for the user.
	 * 
	 * @throws SmackException
	 */
	void logout() throws SmackException { xmppSession.logout(); }

	/**
	 * Process the remote offline queue.
	 * 
	 * @throws SmackException
	 */
	void processOfflineQueue() throws SmackException {
		xmppSession.processOfflineQueue();
	}

	/**
	 * Obtain a list of contacts for an artifact.
	 * 
	 * @param uniqueId
	 *            The artifact unqique id.
	 * @return A list of contacts.
	 * @throws SmackException
	 */
	Set<User> readArtifactTeam(final UUID uniqueId) throws SmackException {
		return xmppSession.readArtifactTeam(uniqueId);
	}

	/**
	 * Read the logged in user's contacts.
	 * 
	 * @return A list of contacts.
	 * @throws SmackException
	 */
	Set<Contact> readContacts() throws SmackException {
		return xmppSession.readContacts();
	}

    /**
     * Read a set of users.
     * 
     * @param jabberIds
     *            The user ids to read.
     * @return A set of users.
     * @throws SmackException
     */
	Set<User> readUsers(final Set<JabberId> jabberIds) throws SmackException {
		return xmppSession.readUsers(jabberIds);
	}

	/**
	 * Send a close packet to the parity server.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws SmackException
	 */
	void sendClose(final UUID artifactUniqueId) throws SmackException {
		xmppSession.closeArtifact(artifactUniqueId);
	}

	/**
	 * Send a create packet to the parity server.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws SmackException
	 */
	void sendCreate(final UUID artifactUniqueId) throws SmackException {
		xmppSession.createArtifact(artifactUniqueId);
	}

	/**
	 * Send a deletion packet to the parity server.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws SmackException
	 */
	void sendDelete(final UUID artifactUniqueId) throws SmackException {
		xmppSession.removeArtifactTeamMember(artifactUniqueId);
	}

	/**
     * Reactivate a document.
     *
     */
    void sendDocumentReactivate(final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] bytes)
            throws SmackException {
        xmppSession.sendDocumentReactivate(team, uniqueId, versionId, name, bytes);
    }

	/**
     * Send a document.
     * 
     * @param sendTo
     *            The ids to send to.
     * @param uniqueId
     *            The document unique id.
     * @param name
     *            The document name.
     * @param content
     *            The document content.
     * @throws SmackException
     */
	void sendDocumentVersion(final Set<JabberId> sendTo, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws SmackException {
		xmppSession.sendDocumentVersion(sendTo, uniqueId, versionId, name, content);
	}

	/**
	 * Send a reqest for a document key to the parity server.
	 * 
	 * @param artifactUniqueId
	 *            The object unique id.
	 * @throws ParityException
	 */
	void sendKeyRequest(final UUID artifactUniqueId) throws SmackException {
		xmppSession.requestArtifactKey(artifactUniqueId);
	}

	/**
	 * Send the response to a document key request to the user (via the parity
	 * server).
	 * 
	 * @param artifactUniqueId
	 *            The document unique id.
	 * @param keyResponse
	 *            The response.
	 * @param user
	 *            The user.
	 * @throws SmackException
	 */
	void sendKeyResponse(final UUID artifactUniqueId, final KeyResponse keyResponse,
			final User user) throws SmackException {
		xmppSession.sendKeyResponse(artifactUniqueId, keyResponse, user);
	}

    /**
	 * Send the log file archive to the parity server.
	 * 
	 * @param logFileArchive
	 *            The log file archive.
	 * @param The
	 *            user to send the file to.
	 * @throws SmackException
	 */
	void sendLogFileArchive(final File logFileArchive, final User user)
			throws SmackException {
		xmppSession.sendLogFileArchive(logFileArchive, user);
	}

	/**
	 * Send a subscribe packet to the parity server.
	 * 
	 * @param artifactUniqueId
	 *            The object unique id.
	 * @throws SmackException
	 */
	void sendSubscribe(final UUID artifactUniqueId) throws SmackException {
		xmppSession.addArtifactTeamMember(artifactUniqueId);
	}

    /**
     * Update the user.
     * 
     * @param name
     *            The user's name.
     * @param email
     *            The user's email.
     * @param organization
     *            The user's organization.
     * @throws SmackException
     */
    void updateUser(final String name, final String email,
            final String organization) throws SmackException {
        xmppSession.updateCurrentUser(name, email, organization);
    }

	/**
	 * Event handler for the extension listener's artifact close event.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param artifactClosedBy
	 *            The user who closed the artfiact.
	 */
	private void handleArtifactClosed(final UUID artifactUniqueId,
			final JabberId artifactClosedBy) {
		try {
			SessionModelImpl.notifyArtifactClosed(
					artifactUniqueId, artifactClosedBy);
		}
		catch(final ParityException px) { unexpectedOccured(px); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

    /**
     * Event handler for confirmation receipts.
     * 
     * @param uniqueId
     *      The document unique id.
     * @param versionId
     *      The document version id.
     * @param receivedFrom
     *      From whom the confirmation was sent.
     */
    private void handleConfirmationReceipt(final UUID uniqueId,
            final Long versionId, final JabberId receivedFrom) {
        try {
            SessionModelImpl.notifyConfirmationReceipt(
                    uniqueId, versionId, receivedFrom);
        }
        catch(final ParityException px) { unexpectedOccured(px); }
        catch(final SmackException sx) { unexpectedOccured(sx); }
        catch(final RuntimeException rx) { unexpectedOccured(rx); }
    }

    /**
     * Event handler for the extension listener's document reactivated event.
     * 
     * @param reactivatedBy
     *            By whom the document was reactivated.
     * @param team
     *            The team.
     * @param uniqueId
     *            The unique id.
     * @param versionId
     *            The version id.
     * @param name
     *            The name.
     * @param content
     *            The content.
     */
    private void handleDocumentReactivated(final JabberId reactivatedBy,
            final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content) {
        try {
            SessionModelImpl.notifyDocumentReactivated(reactivatedBy, team,
                    uniqueId, versionId, name, content);
        }
        catch(final ParityException px) { unexpectedOccured(px); }
        catch(final RuntimeException rx) { unexpectedOccured(rx); }
    }

    /**
	 * Event handler for the extension listener's document received event.
	 * 
	 * @param xmppDocument
	 *            The xmpp document that has been received.
	 */
	private void handleDocumentReceived(final JabberId receivedFrom, final UUID uniqueId, final Long versionId, final String name, final byte[] content) {
		try {
			SessionModelImpl
                .notifyDocumentReceived(receivedFrom, uniqueId, versionId, name,
                        content);
		}
        catch(final ParityException px) { unexpectedOccured(px); }
        catch(final SmackException sx) { unexpectedOccured(sx); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

    private void handleInvitationAccepted(final JabberId acceptedBy) {
		try { SessionModelImpl.notifyInvitationAccepted(acceptedBy); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	private void handleInvitationDeclined(final JabberId declinedBy) {
		try { SessionModelImpl.notifyInvitationDeclined(declinedBy); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	private void handleInvitationExtended(final JabberId invitedBy) {
		try { SessionModelImpl.notifyInvitationExtended(invitedBy); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	/**
	 * Event handler for the extension listener's key request accepted event.
	 * 
	 * @param user
	 *            The user who accepted the request.
	 * @param artifactUniqueId
	 *            The artifact unique id being requested.
	 */
	private void handleKeyRequestAccepted(final UUID artifactUniqueId,
			final JabberId acceptedBy) {
		try {
			SessionModelImpl.notifyKeyRequestAccepted(
					artifactUniqueId, acceptedBy);
		}
		catch(final ParityException px) { unexpectedOccured(px); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
		catch(final SmackException sx) { unexpectedOccured(sx); }
	}

	/**
	 * Event handler for the extension listener's key request accepted event.
	 * 
	 * @param user
	 *            The user who accepted the request.
	 * @param artifactUniqueId
	 *            The artifact unique id being requested.
	 */
	private void handleKeyRequestDenied(final UUID artifactUniqueId,
			final JabberId deniedBy) {
		try {
			SessionModelImpl.notifyKeyRequestDenied(artifactUniqueId, deniedBy);
		}
		catch(final ParityException px) { unexpectedOccured(px); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
		catch(final SmackException sx) { unexpectedOccured(sx); }
	}

	/**
	 * Event handler for the extension listener's key requested event.
	 * 
	 * @param user
	 *            The user requesting the key.
	 * @param artifactUniqueId
	 *            The artifact unique id being requested.
	 */
	private void handleKeyRequested(final UUID artifactUniqueId,
			final JabberId requestedBy) {
		try {
			SessionModelImpl.notifyKeyRequested(artifactUniqueId, requestedBy);
		}
		catch(final ParityException px) { unexpectedOccured(px); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
		catch(final SmackException sx) { unexpectedOccured(sx); }
	}

	/**
	 * Event handler for the session listener's session established event.
	 *
	 */
	private void handleSessionEstablished() {
		try { SessionModelImpl.notifySessionEstablished(); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	/**
	 * Event handler for the session listener's session termination event.
	 * 
	 */
	private void handleSessionTerminated() {
		try { SessionModelImpl.notifySessionTerminated(); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	/**
	 * Event handler for the sesion listener's session termination with error
	 * event.
	 * 
	 * @param x
	 *            The error.
	 */
	private void handleSessionTerminated(final Exception x) {
		try { SessionModelImpl.notifySessionTerminated(x); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	/**
     * Handle the event that occurs when a team member is added.
     * 
     * @param teamMember
     *            The new team member.
     */
	private void handleTeamMemberAdded(final UUID artifactUniqueId,
			final Contact teamMember) {
		try { SessionModelImpl.notifyTeamMemberAdded(artifactUniqueId, teamMember); }
		catch(final ParityException px) { unexpectedOccured(px); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	/**
     * Handle the event that occurs when a team member is removed.
     * 
     * @param teamMember
     *            The team member.
     */
	private void handleTeamMemberRemoved(final UUID artifactUniqueId,
			final Contact teamMember) {
		try { SessionModelImpl.notifyTeamMemberRemoved(artifactUniqueId, teamMember); }
		catch(final ParityException px) { unexpectedOccured(px); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	private void unexpectedOccured(final ParityException px) {
		// TODO  Implement rModel rollback.
		logger.fatal("[LMODEL] [SESSION] [XMPP] [HANDLE REVENT]", px);
	}

	private void unexpectedOccured(final RuntimeException rx) {
		// TODO  Implement rModel rollback.
		logger.fatal("[LMODEL] [SESSION] [XMPP] [HANDLE REVENT]", rx);
	}

	private void unexpectedOccured(final SmackException sx) {
		// TODO  Implement rModel rollback.
		logger.fatal("[LMODEL] [SESSION] [XMPP] [HANDLE REVENT]", sx);
	}
}
