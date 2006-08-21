/*
 * Oct 9, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImplHelper;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.XMPPSession;
import com.thinkparity.model.xmpp.XMPPSessionFactory;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.events.XMPPContactListener;
import com.thinkparity.model.xmpp.events.XMPPContainerListener;
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

	/** An xmpp artifact event listener. */
	private final XMPPArtifactListener xmppArtifactListener;

    /** An xmpp container event listener. */
    private final XMPPContainerListener xmppContainerListener;

    /** An xmpp document event listener. */
    private final XMPPDocumentListener xmppDocumentListener;

	/**An xmpp extension listener. */
	private final XMPPExtensionListener xmppExtensionListener;

	/** An xmpp presence listener. */
	private final XMPPContactListener xmppPresenceListener;

	/** The xmpp session. */
	private final XMPPSession xmppSession;

	/** An xmpp Session listener. */
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
			public void teamMemberAdded(final UUID uniqueId,
                    final JabberId jabberId) {
                handleTeamMemberAdded(uniqueId, jabberId);
            }
            public void teamMemberRemoved(final UUID artifactUniqueId,
                    final JabberId jabberId) {
				handleTeamMemberRemoved(artifactUniqueId, jabberId);
			}
		};
        this.xmppContainerListener = new XMPPContainerListener() {
            public void handleArtifactPublished(final JabberId publishedBy,
                    final Calendar publishedOn, final UUID containerUniqueId,
                    final Long containerVersionId, final String containerName,
                    final Integer containerArtifactCount,
                    final Integer containerArtifactIndex,
                    final UUID artifactUniqueId, final Long artifactVersionId,
                    final String artifactName, final ArtifactType artifactType,
                    final String artifactChecksum, final byte[] artifactBytes) {
                handleContainerArtifactPublished(publishedBy, publishedOn,
                        containerUniqueId, containerVersionId, containerName,
                        containerArtifactCount, containerArtifactIndex,
                        artifactUniqueId, artifactVersionId, artifactName,
                        artifactType, artifactChecksum, artifactBytes);
            }
            public void handleArtifactSent(final JabberId sentBy,
                    final Calendar sentOn, final UUID containerUniqueId,
                    final Long containerVersionId, final String containerName,
                    final Integer containerArtifactCount,
                    final Integer containerArtifactIndex,
                    final UUID artifactUniqueId, final Long artifactVersionId,
                    final String artifactName, final ArtifactType artifactType,
                    final String artifactChecksum, final byte[] artifactBytes) {
                handleContainerArtifactSent(sentBy, sentOn, containerUniqueId,
                        containerVersionId, containerName,
                        containerArtifactCount, containerArtifactIndex,
                        artifactUniqueId, artifactVersionId, artifactName,
                        artifactType, artifactChecksum, artifactBytes);
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
                Assert.assertUnreachable("SessionModelXMPPHelper$XMPPDocumentListener#documentReceived()");
            }
        };
		this.xmppExtensionListener = new XMPPExtensionListener() {
			public void keyRequestAccepted(final UUID artifactUniqueId,
					final JabberId acceptedBy) {
				handleKeyRequestAccepted(artifactUniqueId, acceptedBy);
			}
			public void keyRequestDenied(final UUID artifactUniqueId,
					final JabberId deniedBy) {
				handleKeyRequestDenied(artifactUniqueId, deniedBy);
			}
			public void keyRequested(final UUID artifactUniqueId, final JabberId requestedBy) {
				Assert.assertUnreachable("SessionModelXMPPHelper$XMPPExtensionListener#keyRequested()");
			}
		};
		this.xmppPresenceListener = new XMPPContactListener() {
			public void handleInvitationAccepted(final JabberId acceptedBy,
                    final Calendar acceptedOn) {
				handleContactInvitationAccepted(acceptedBy, acceptedOn);
			}
			public void handleInvitationDeclined(final String invitedAs,
                    final JabberId declinedBy, final Calendar declinedOn) {
				handleContactInvitationDeclined(invitedAs, declinedBy, declinedOn);
			}
			public void handleInvitationExtended(final String invitedAs,
                    final JabberId invitedBy, final Calendar invitedOn) {
				handleContactInvitationExtended(invitedAs, invitedBy, invitedOn);
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
        xmppSession.addListener(xmppContainerListener);
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
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws SmackException
     */
    void addTeamMember(final UUID uniqueId, final JabberId jabberId)
            throws SmackException {
        xmppSession.addTeamMember(uniqueId, jabberId);
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
     * Create an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
	void createArtifact(final UUID uniqueId) throws SmackException {
		xmppSession.createArtifact(uniqueId);
	}

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void createDraft(final UUID uniqueId) { xmppSession.createDraft(uniqueId); }

	/**
	 * Decline an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws SmackException
	 */
	void declineInvitation(final String invitedAs, final JabberId jabberId) throws SmackException {
		xmppSession.declineInvitation(invitedAs, jabberId);
	}

	/**
     * Delete an artifact.
     * 
     * @param uniqueId
     *            A unique id.
     */
    void deleteArtifact(final UUID uniqueId) {
        xmppSession.deleteArtifact(uniqueId);
    }

	/**
	 * Obtain the user for the current session.
	 * 
	 * @return The user for the current session.
	 */
	User getUser() throws SmackException { return xmppSession.readCurrentUser(); }

	/**
     * Obtain the xmpp session.
     * 
     * @return The xmpp session.
     */
    XMPPSession getXMPPSession () {
        return xmppSession;
    }

	/**
	 * Send a contact invitation.
	 * 
	 * @param email
	 *            An e-mail address.
	 * @throws SmackException
	 */
	void inviteContact(final String email) throws SmackException {
		xmppSession.inviteContact(email);
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
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    Contact readContact(final JabberId contactId) throws SmackException {
        return xmppSession.readContact(contactId);
    }

    /**
	 * Read the logged in user's contacts.
	 * 
	 * @return A list of contacts.
	 * @throws SmackException
	 */
	List<Contact> readContacts() throws SmackException {
		return xmppSession.readContacts();
	}

	/**
	 * Obtain the artifact key holder.
	 * 
	 * @param uniqueId
	 *            The artifact unique id.
	 * @return A jabber id.
	 */
	JabberId readKeyHolder(final UUID uniqueId) {
		return xmppSession.readKeyHolder(uniqueId);
	}

	/**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
	Profile readProfile() throws SmackException { return xmppSession.readProfile(); }

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
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        xmppSession.removeTeamMember(uniqueId, jabberId);
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
     * Update the user.
     * 
     * @param user
     *            The user.
     * @throws SmackException
     */
    void updateUser(final User user) throws SmackException {
        xmppSession.updateUser(user);
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

    private void handleContactInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn) {
		try {
            SessionModelImpl.handleContactInvitationAccepted(acceptedBy, acceptedOn);
		} catch (final RuntimeException rx) {
            unexpectedOccured(rx);
        }
	}

    private void handleContactInvitationDeclined(final String invitedAs,
            final JabberId declinedBy, final Calendar declinedOn) {
		try {
            SessionModelImpl.handleContactInvitationDeclined(invitedAs, declinedBy, declinedOn);
		} catch (final RuntimeException rx) {
            unexpectedOccured(rx);
		}
	}

	/**
     * Handle the contact invitation extended remote event.
     * 
     * @param invitedBy
     *            By whom the invitation was extended.
     * @param invitedOn
     *            When the invitation was extended.
     */
	private void handleContactInvitationExtended(final String invitedAs,
            final JabberId invitedBy, final Calendar invitedOn) {
		try { SessionModelImpl.handleInvitationExtended(invitedAs,invitedBy, invitedOn); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

	/**
     * Handle the artifact published event for the container.
     * 
     * @param containerUniqueId
     *            The container unique id.
     * @param containerVersionId
     *            The container version id.
     * @param count
     *            The artifact count.
     * @param index
     *            The artifact index.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     * @param type
     *            The artifact type.
     * @param bytes
     *            The artifact bytes.
     */
    private void handleContainerArtifactPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes) {
        try {
            SessionModelImpl.handleContainerArtifactPublished(publishedBy,
                    publishedOn, containerUniqueId, containerVersionId,
                    containerName, containerArtifactCount,
                    containerArtifactIndex, artifactUniqueId,
                    artifactVersionId, artifactName, artifactType,
                    artifactChecksum, artifactBytes);
        }
        catch(final ParityException px) { unexpectedOccured(px); }
    }

	/**
     * Handle the artifact sent event for the container.
     * 
     * @param containerUniqueId
     *            The container unique id.
     * @param containerVersionId
     *            The container version id.
     * @param count
     *            The artifact count.
     * @param index
     *            The artifact index.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     * @param type
     *            The artifact type.
     * @param bytes
     *            The artifact bytes.
     */
    private void handleContainerArtifactSent(final JabberId sentBy,
            final Calendar sentOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes) {
        try {
            SessionModelImpl.handleContainerArtifactSent(sentBy, sentOn,
                    containerUniqueId, containerVersionId, containerName,
                    containerArtifactCount, containerArtifactIndex,
                    artifactUniqueId, artifactVersionId, artifactName,
                    artifactType, artifactChecksum, artifactBytes);
        }
        catch(final ParityException px) { unexpectedOccured(px); }
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
     * Handle the team member added remote event.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
	private void handleTeamMemberAdded(final UUID uniqueId, final JabberId jabberId) {
		try { SessionModelImpl.handleTeamMemberAdded(uniqueId, jabberId); }
		catch(final RuntimeException rx) { unexpectedOccured(rx); }
	}

    /**
     * Handle the team member removed remote event.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
	private void handleTeamMemberRemoved(final UUID uniqueId,
            final JabberId jabberId) {
		try { SessionModelImpl.handleTeamMemberRemoved(uniqueId, jabberId); }
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
