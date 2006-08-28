/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.KeyListener;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.container.InternalContainerModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * The implementation of the parity session interface.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.37
 */
class SessionModelImpl extends AbstractModelImpl {

    /**
	 * List of all of the registered parity key listeners.
	 * 
	 */
	private static final Collection<KeyListener> keyListeners;

	/**
	 * List of all of the registered parity presence listeners.
	 * 
	 * @see SessionModelImpl#presenceListenersLock
	 */
	private static final Collection<PresenceListener> presenceListeners;

	/**
	 * Lock used to synchronize the collection access.
	 * 
	 * @see SessionModelImpl#presenceListeners
	 */
	private static final Object presenceListenersLock;

	/**
	 * The static session model context.
	 * 
	 */
	private static final Context sContext;

	/**
	 * List of all of the registered parity session listeners.
	 * 
	 * @see SessionModelImpl#sessionListenersLock
	 */
	private static final Collection<SessionListener> sessionListeners;

	/**
	 * Lock used to synchronize the collection access.
	 * 
	 * @see SessionModelImpl#sessionListeners
	 */
	private static final Object sessionListenersLock;

	/**
	 * Helper wrapper class for xmpp calls.
	 * 
	 * @see SessionModelImpl#xmppHelperLock
	 */
	private static final SessionModelXMPPHelper xmppHelper;

	static {
		// create the key listener list & sync lock
		keyListeners = new Vector<KeyListener>(3);
		// create the presence listener list & sync lock
		presenceListeners = new Vector<PresenceListener>(3);
		presenceListenersLock = new Object();
		// create the session listener list & sync lock
		sessionListeners = new Vector<SessionListener>(3);
		sessionListenersLock = new Object();
		// session context
		sContext = getSessionModelContext();
		// create the xmpp helper
		xmppHelper = new SessionModelXMPPHelper();
	}

    /**
     * Handle the artifact draft created remote event.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param createdBy
     *            The creation user.
     * @param deletedOn
     *            The creation date.
     */
    static void handleArtifactDraftCreated(final UUID uniqueId,
            final JabberId createdBy, final Calendar createdOn) {
        final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        artifactModel.handleDraftCreated(uniqueId, createdBy, createdOn);
    }

    /**
     * Handle the artifact draft deleted remote event.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param deletedBy
     *            The deletion user.
     * @param deletedOn
     *            The deletion date.
     */
    static void handleArtifactDraftDeleted(final UUID uniqueId,
            final JabberId deletedBy, final Calendar deletedOn) {
        final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        artifactModel.handleDraftDeleted(uniqueId, deletedBy, deletedOn);
    }

    /**
     * Handle the acceptance of an invitation.
     * 
     * @param acceptedBy
     *            By whom the invitation was accepted.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    static void handleContactInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn) {
        ContactModel.getInternalModel(sContext).handleInvitationAccepted(acceptedBy, acceptedOn);
    }

    /**
     * Handle the declination of an invitation.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param declinedBy
     *            By whom the invitation was declined.
     * @param declinedOn
     *            When the invitation was declined.
     */
    static void handleContactInvitationDeclined(final EMail invitedAs,
            final JabberId declinedBy, final Calendar declinedOn) {
        ContactModel.getInternalModel(sContext).handleInvitationDeclined(invitedAs, declinedBy, declinedOn);
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
    static void handleContainerArtifactPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes) throws ParityException {
        final InternalContainerModel containerModel = ContainerModel.getInternalModel(sContext);
        containerModel.handleArtifactPublished(containerUniqueId, containerVersionId,
                containerName, artifactUniqueId, artifactVersionId,
                artifactName, artifactType, artifactChecksum,
                artifactBytes, publishedBy, publishedOn);
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
    static void handleContainerArtifactSent(final JabberId sentBy,
            final Calendar sentOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes) throws ParityException {
        final InternalContainerModel containerModel = ContainerModel.getInternalModel(sContext);
        containerModel.handleArtifactSent(containerUniqueId, containerVersionId, containerName,
                artifactUniqueId, artifactVersionId, artifactName,
                artifactType, artifactChecksum, artifactBytes,
                sentBy, sentOn);
    }

    /**
     * Handle the container published event.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param artifactCount
     *            The container artifact count.
     * @param publishedBy
     *            The publisher.
     * @param publishedTo
     *            The publishees.
     * @param publishedOn
     *            The publish date.
     */
    static void handleContainerPublished(final UUID uniqueId,
            final Long versionId, final String name,
            final Integer artifactCount, final JabberId publishedBy,
            final List<JabberId> publishedTo, final Calendar publishedOn) {
        final InternalContainerModel containerModel = ContainerModel.getInternalModel(sContext);
        containerModel.handlePublished(uniqueId, versionId, name,
                artifactCount, publishedBy, publishedTo, publishedOn);
    }

    /**
     * Handle the container sent event.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param name
     *            A container name <code>String</code>.
     * @param artifactCount
     *            An artifact count <code>Integer</code>.
     * @param sentBy
     *            The sent by user <code>JabberId</code>.
     * @param sentOn
     *            The sent on date <code>Calendar</code>.
     * @param sentTo
     *            The sent to <code>List&lt;JabberId&gt;</code>.
     */
    static void handleContainerSent(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId sentBy, final Calendar sentOn,
            final List<JabberId> sentTo) {
        final InternalContainerModel containerModel = ContainerModel.getInternalModel(sContext);
        containerModel.handleSent(uniqueId, versionId, name, artifactCount,
                sentBy, sentOn, sentTo);
    }

	/**
     * Create an incoming invitation for the user.
     * 
     * @param invitedBy
     *            From whome the invitation was extended.
     */
	static void handleInvitationExtended(final EMail invitedAs,
            final JabberId invitedBy, final Calendar invitedOn) {
		ContactModel.getInternalModel(sContext).handleInvitationExtended(invitedAs, invitedBy, invitedOn);
	}

    /**
     * Handle the event that a new team member was added to the artifact.
     * 
     * @param artifactUniqueId
     *            The artifact's unique id.
     * @param newTeamMember
     *            The new team member.
     * @throws ParityException
     */
	static void handleTeamMemberAdded(final UUID uniqueId,
            final JabberId jabberId) {
	    final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        artifactModel.handleTeamMemberAdded(uniqueId, jabberId);
	}

	/**
     * Handle the team member removed remote event.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
	static void handleTeamMemberRemoved(final UUID uniqueId,
			final JabberId jabberId) {
        final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        artifactModel.handleTeamMemberRemoved(uniqueId, jabberId);
	}

    /**
     * Handle the even generated when the user an artifact was sent to has
     * confirmed receipt of the artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param receivedBy
     *            From whom the artifact was received.
     */
    static void notifyConfirmationReceipt(final UUID uniqueId,
            final Long versionId, final JabberId confirmedBy)
            throws ParityException, SmackException {
        final InternalDocumentModel iDModel = DocumentModel.getInternalModel(sContext);
        final Document d = iDModel.get(uniqueId);
        iDModel.confirmSend(d.getId(), versionId, confirmedBy);
    }

    static void notifyDocumentReactivated(final JabberId reactivatedBy,
            final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws ParityException {
        Assert.assertNotYetImplemented("SessionModelImpl#notifyDocumentReactivated");
    }

	/**
     * Handle the event generated by the xmpp extension listener. Here we pass
     * on the event to the key listener.
     * 
     * @param uniqueId
     *            A document id.
     * @param acceptedBy
     *            A user id.
     */
	static void notifyKeyRequestAccepted(final UUID uniqueId,
			final JabberId acceptedBy) throws SmackException, ParityException {
        // assuming document
        final Document d = DocumentModel.getInternalModel(sContext).get(uniqueId);
        DocumentModel.getInternalModel(sContext).keyRequestAccepted(d.getId(), acceptedBy);
	}

	/**
	 * Handle the event generated by the xmpp extension listener. Here we pass
	 * on the notification to the key listener.
	 * 
	 * @param uniqueId
	 *            The document id.
	 * @param deniedBy
	 *            The user id.
	 */
	static void notifyKeyRequestDenied(final UUID uniqueId,
            final JabberId deniedBy) throws ParityException, SmackException {
        final Document d = DocumentModel.getInternalModel(sContext).get(uniqueId);
        DocumentModel.getInternalModel(sContext).keyRequestDeclined(d.getId(), deniedBy);
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * established.
	 *
	 */
	static void notifySessionEstablished() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionEstablished();
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * terminated.
	 *
	 */
	static void notifySessionTerminated() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated();
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * terminated due to an error.
	 * 
	 * @param x
	 *            The cause of the session termination.
	 */
	static void notifySessionTerminated(final Exception x) {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated(x);
			}
		}
	}

	private static StringBuffer getApiId(final String api) {
        return getModelId("SESSION").append(" ").append(api);
    }

	private static StringBuffer getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error);
    }

	/** The remote environment. */
    private final Environment environment;

	/**
	 * Create a SessionModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	SessionModelImpl(final Workspace workspace) {
		super(workspace);
        this.environment = new Environment();
        this.environment.setServerHost(workspace.getPreferences().getServerHost());
        this.environment.setServerPort(workspace.getPreferences().getServerPort());
	}

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId contactId) {
        logApiId();
        logVariable("contactId", contactId);
        try {
            synchronized (xmppHelper) {
                return xmppHelper.readContact(contactId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Accept an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	void acceptInvitation(final JabberId invitedBy) {
	    try {
	        synchronized (xmppHelper) {
                xmppHelper.acceptInvitation(invitedBy);
	        }
        } catch (final Throwable t) {
            throw translateError(t);
	    }
	}

	/**
	 * Add a key listener to the session.
	 * 
	 * @param keyListener
	 *            The key listener to add.
	 */
	void addListener(final KeyListener keyListener) {
		Assert.assertNotNull("Cannot register a null key listener.", keyListener);
		synchronized(keyListeners) {
			Assert.assertNotTrue(
					"Cannot re-registry the same key listener.",
					SessionModelImpl.keyListeners.contains(keyListener));
			SessionModelImpl.keyListeners.add(keyListener);
		}
	}

    /**
	 * Add a presence listener to the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to add.
	 */
	void addListener(final PresenceListener presenceListener) {
		Assert.assertNotNull("Cannot register a null presence listener.",
				presenceListener);
		synchronized(SessionModelImpl.presenceListenersLock) {
			Assert.assertTrue("Cannot re-register the same presence listener.",
					!SessionModelImpl.presenceListeners.contains(presenceListener));
			SessionModelImpl.presenceListeners.add(presenceListener);
		}
	}

    /**
	 * Add a session listener to the session.
	 * 
	 * @param sessionListener
	 *            The session listener to add.
	 */
	void addListener(final SessionListener sessionListener) {
		Assert.assertNotNull("Cannot register a null session listener.",
				sessionListener);
		Assert.assertTrue("Cannot re-register the same session listener.",
				!sessionListeners.contains(sessionListener));
		sessionListeners.add(sessionListener);
	}

    /**
     * Add an email to a user's profile.
     * 
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    void addProfileEmail(final JabberId userId, final ProfileEMail email) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        synchronized (xmppHelper) {
            xmppHelper.getXMPPSession().addProfileEmail(userId, email.getEmail());
        }
    }

    /**
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     */
    void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        try {
            synchronized (xmppHelper) {
                xmppHelper.addTeamMember(uniqueId, jabberId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Send an artifact received confirmation receipt.
	 * 
	 * @param receivedFrom
	 *            From whom the artifact was received.
	 * @param uniqueId
	 *            The artifact unique id.
	 */
	void confirmArtifactReceipt(final JabberId receivedFrom,
	        final UUID uniqueId, final Long versionId) throws SmackException {
	    logger.info("[LMODEL] [SESSION] [CONFIRM ARTIFACT RECEIPT]");
	    logger.debug(receivedFrom);
	    logger.debug(uniqueId);
        logger.debug(versionId);
	    synchronized(xmppHelper) {
	        assertIsLoggedIn(
	                "[LMODEL] [SESSION] [CONFIRM ARTIFACT RECEIPT]",
	                xmppHelper);
	        xmppHelper.confirmArtifactReceipt(receivedFrom, uniqueId, versionId);
	    }
	}

	/**
     * Send an artifact creation packet to the parity server.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
	void createArtifact(final UUID uniqueId) {
		logApiId();
		logVariable("uniqueId", uniqueId);
		try {
		    synchronized (xmppHelper) {
		        xmppHelper.createArtifact(uniqueId);
		    }
		} catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void createDraft(final UUID uniqueId) {
        logger.info(getApiId("[CREATE DRAFT]"));
        logger.debug(uniqueId);
        assertOnline(getApiId("[CREATE DRAFT] [USER NOT ONLINE]"));
        synchronized(xmppHelper) { xmppHelper.createDraft(uniqueId); }
    }

	/**
	 * Decline a user's invitation to their contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	void declineInvitation(final EMail invitedAs, final JabberId invitedBy) {
        logApiId();
        logVariable("invitedAs", invitedAs);
        logVariable("invitedBy", invitedBy);
		try {
		    synchronized(xmppHelper) {
		        xmppHelper.getXMPPSession().declineInvitation(invitedAs, invitedBy);
            }
		} catch(final Throwable t) {
            throw translateError(t);
		}
	}

	/**
     * Delete an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteArtifact(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        try {
            synchronized (xmppHelper) {
                xmppHelper.deleteArtifact(uniqueId);
            }
        }
        catch(final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Delete a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        synchronized (xmppHelper) {
            xmppHelper.getXMPPSession().deleteDraft(uniqueId);
        }
    }

    /**
	 * Invite a contact.
	 * 
	 * @param email
	 *            An e-mail address.
	 */
	void inviteContact(final EMail email) {
        logApiId();
        logVariable("email", email);
		try {
		    synchronized (xmppHelper) {
		        xmppHelper.getXMPPSession().inviteContact(email);
		    }
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

	/**
	 * Determine whether or not a user is logged in.
	 * 
	 * @return True if the user is logged in, false otherwise.
	 */
	Boolean isLoggedIn() {
		synchronized(xmppHelper) { return xmppHelper.isLoggedIn(); }
	}

    /**
	 * Determine whether or not the logged in user is the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the logged in user is the artifact key holder; false
	 *         otherwise.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             If the user is offline.
	 */
	Boolean isLoggedInUserKeyHolder(final Long artifactId) {
		logger.info("isLoggedInUserKeyHolder(Long)");
		logger.debug(artifactId);
		try {
		    final UUID uniqueId = readArtifactUniqueId(artifactId);
		    synchronized(xmppHelper) {
				final JabberId keyHolder = xmppHelper.readKeyHolder(uniqueId);
				return keyHolder.equals(localUserId());
		    }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Establish a new xmpp session.
     * 
     * @throws ParityException
     */
    void login() {
        logger.info(getApiId("[LOGIN]"));
        login(readCredentials());
    }

    /**
     * Establish a new xmpp session.
     * 
     * @param credentials
     *            The user's credentials.
     * @throws ParityException
     */
    void login(final Credentials credentials) {
        logger.info(getApiId("[LOGIN]"));
        logger.debug(credentials);
        login(environment, credentials);
    }

    /**
	 * Terminate the current session.
	 * 
	 * @throws ParityException
	 */
	void logout() {
		try {
		    assertOnline(getApiId("[LOGOUT]"));
		    synchronized(xmppHelper) {
		        xmppHelper.logout();
            }
		}
		catch(final Throwable t) {
			throw translateError(t);
		}
	}

	/**
     * Publish a container.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents and their content.
     * @param publishTo
     *            A list of ids to publish to.
     * @param publishedBy
     *            By whom the container was published.
     * @param publishedOn
     *            When the container was published.
     */
    void publish(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) {
        logApiId();
        logVariable("container", container);
        logVariable("documents", documents);
        logVariable("publishTo", publishTo);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        try {
            synchronized (xmppHelper) {
                xmppHelper.getXMPPSession().publish(container, documents,
                        publishTo, publishedBy, publishedOn);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A list of jabber ids.
     */
	List<JabberId> readArtifactTeam(final UUID uniqueId) {
		logApiId();
        logVariable("uniqueId", uniqueId);
		try {
		    synchronized (xmppHelper) {
    		    /* TODO Change this xmpp session api to return a list of jabber
                 * ids */
    			final List<User> teamUsers =
                    xmppHelper.getXMPPSession().readArtifactTeam(uniqueId);
                final List<JabberId> team = new ArrayList<JabberId>(teamUsers.size());
                for (final User user : teamUsers)
                    team.add(user.getId());
                return team;
		    }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Read the session user's contact info.
     * 
     * @return The user's contact info.
     */
    Contact readContact() {
        logger.info(getApiId("[READ CONTACT]"));
        throw Assert.createNotYetImplemented("SessionModelImpl#readContact()");
    }

    /**
     * Read the logged in user's contacts.
     * 
     * @return A set of contacts.
     * @throws ParityException
     */
	List<Contact> readContacts() {
		try {
		    synchronized(xmppHelper) {
		        return xmppHelper.readContacts(); 
		    }
        } catch (final Throwable t) {
            throw translateError(t);
		}
	}

	/**
     * Read the artifact key holder from the server.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    JabberId readKeyHolder(final UUID uniqueId) {
		logApiId();
        logVariable("uniqueId", uniqueId);
		try {
		    synchronized (xmppHelper) {
		        return xmppHelper.readKeyHolder(uniqueId);
		    }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    Profile readProfile() {
        logger.info(getApiId("[READ PROFILE]"));
        try {
            assertOnline(getApiId("[READ PROFILE] [USER NOT ONLINE]"));
            synchronized(xmppHelper) {
                return xmppHelper.readProfile();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<ProfileEMail> readProfileEMails() {
        logApiId();
        try {
            synchronized (xmppHelper) {
                return xmppHelper.getXMPPSession().readProfileEMails();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the logged in user's session.
     * 
     * @return The logged in user's session.
     */
    Session readSession() {
        logger.info(getApiId("[READ SESSION]"));
        try {
            assertOnline(getApiId("[READ SESSION] [USER NOT ONLINE]"));
            final Session session = new Session();
            session.setJabberId(xmppHelper.getUser().getId());
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the session user's user info.
     * 
     * @return thinkParity user info.
     * @throws ParityException
     */
	User readUser() throws ParityException {
        logger.info(getApiId("[READ USER]"));
        assertOnline(getApiId("[READ USER] [USER NOT ONLINE]"));
        synchronized(xmppHelper) {
            try { return xmppHelper.getUser(); }
            catch(final SmackException sx) {
                logger.error(getApiId("[READ USER] [SMACK ERROR]"), sx);
                throw ParityErrorTranslator.translate(sx);
            }
        }

    }

    /**
     * Read a single user.
     * 
     * @param jabberId
     *            The user id.
     * @return The user.
     * @throws ParityException
     */
	User readUser(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
		final Set<JabberId> userIds = new HashSet<JabberId>();
		userIds.add(userId);
		return readUsers(userIds).iterator().next();
	}

    /**
     * Read a set of users.
     * 
     * @param jabberIds
     *            A set of user ids.
     * @return A set of users.
     * @throws ParityException
     */
	Set<User> readUsers(final Set<JabberId> jabberIds) {
        logApiId();
        logVariable("jabberIds", jabberIds);
		try {
		    synchronized (xmppHelper) {
		        return xmppHelper.getXMPPSession().readUsers(jabberIds);
            }
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

	/**
	 * Remove a key listener from the session.
	 * 
	 * @param keyListener
	 *            The key listener to remove.
	 */
	void removeListener(final KeyListener keyListener) {
		Assert.assertNotNull("Cannot remove a null key listener.", keyListener);
		synchronized(SessionModelImpl.keyListeners) {
			Assert.assertTrue(
					"Cannot remove a non-registered listener.",
					SessionModelImpl.keyListeners.contains(keyListener));
			SessionModelImpl.keyListeners.remove(keyListener);
		}
	}

	/**
	 * Remove a presence listener from the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to remove.
	 */
	void removeListener(final PresenceListener presenceListener) {
		logger.info("removeListener(PresenceListener)");
		logger.debug(presenceListener);
		Assert.assertNotNull("Cannot remove a null presence listener.", presenceListener);
		synchronized(SessionModelImpl.presenceListenersLock) {
			Assert.assertTrue(
					"Cannot remove a non-registered listener.",
					SessionModelImpl.presenceListeners.contains(presenceListener));
			SessionModelImpl.presenceListeners.remove(presenceListener);
		}
	}

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 *            The session listener to remove.
	 */
	void removeListener(final SessionListener sessionListener) {
		logger.info("removeListener(SessionListener)");
		logger.debug(sessionListener);
		Assert.assertNotNull("Cannot remove a null session listener.", sessionListener);
		synchronized(SessionModelImpl.sessionListenersLock) {
			Assert.assertTrue(
					"Cannot remove a non-registered listener.",
					SessionModelImpl.sessionListeners.contains(sessionListener));
			SessionModelImpl.sessionListeners.remove(sessionListener);
		}
	}

	/**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    void removeProfileEmail(final JabberId userId, final ProfileEMail email) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        synchronized (xmppHelper) {
            xmppHelper.getXMPPSession().removeProfileEmail(userId, email.getEmail());
        }
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
        logger.info(getApiId("[REMOVE TEAM MEMBER]"));
        logger.debug(uniqueId);
        logger.debug(jabberId);
        synchronized(xmppHelper) {
            xmppHelper.removeTeamMember(uniqueId, jabberId);
        }
    }

    /**
     * Send a container version.
     * 
     * @param version
     *            A container version.
     * @param documentVersions
     *            A list of document versions.
     * @param user
     *            A user.
     */
    void send(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> sendTo, final JabberId sentBy,
            final Calendar sentOn) {
        logApiId();
        logVariable("container", container);
        logVariable("documents", documents);
        logVariable("sendTo", sendTo);
        logVariable("sentBy", sentBy);
        logVariable("sentOn", sentOn);
        try {
            synchronized (xmppHelper) {
                xmppHelper.getXMPPSession().send(container, documents, sendTo,
                        sentBy, sentOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Send the parity log file. To be used in order to troubleshoot remote
	 * problems.
	 * 
	 * @throws ParityException
	 */
	void sendLogFileArchive() throws ParityException {
	    throw Assert
                .createNotYetImplemented("SessionModelImpl#sendLogFileArchive");
	}

    /**
     * Update the session user's contact info.
     * 
     * @param contact
     *            The user's contact info.
     */
    void updateContact(final Contact contact) {
        throw Assert.createNotYetImplemented("SessionModelImpl#updateContact(Contact)");
    }

    /**
     * Update the user's profile.
     * 
     * @param profile
     *            The user's <code>Profile</code>.
     * @throws ParityException
     */
    void updateProfile(final Profile profile) {
        logApiId();
        logVariable("profile", profile);
        try {
            synchronized (xmppHelper) {
                xmppHelper.getXMPPSession().updateProfile(profile);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    void verifyProfileEmail(final JabberId userId,
            final ProfileEMail email, final String key) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        logVariable("key", key);
        try {
            synchronized (xmppHelper) {
                xmppHelper.getXMPPSession().verifyProfileEmail(userId,
                        email.getEmail(), key);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Assert that the user is currently logged in.
     * 
     * @param message
     *            Message to display in the assertion.
     * @param xmppHelper
     *            A handle to the xmpp helper in order to determine logged in
     *            status.
     */
    private void assertIsLoggedIn(final String message,
            final SessionModelXMPPHelper xmppHelper) {
        Assert.assertTrue(message + " [NOT LOGGED IN]", xmppHelper.isLoggedIn());
    }

    /**
     * Login to the environment with the credentials.
     * 
     * @param environment
     *            The environment to login to.
     * @param credentials
     *            The credentials to login with.
     */
    private void login(final Environment environment,
            final Credentials credentials) {
        login(1, environment, credentials);
    }

    /**
     * Login to the environment with the credentials.
     * 
     * @param attempt
     *            The attempt number.
     * @param environment
     *            The environment to login to.
     * @param credentials
     *            The credentials to login with.
     */
    private void login(final Integer attempt, final Environment environment,
            final Credentials credentials) {
        logger.debug(environment);
        logger.debug(credentials);
        try {
            assertNotIsOnline("USER ALREADY ONLINE");
            assertIsReachable("ENVIRONMENT NOT REACHABLE", environment);
            synchronized (xmppHelper) {
                // check that the user's credentials match
                final Credentials storedCredentials = readCredentials();
                if(null != storedCredentials) {
                    Assert.assertTrue(
                            getErrorId("[LOGIN]", "[CANNOT MATCH USER CREDENTIALS]").toString(),
                            storedCredentials.equals(credentials));
                }
                // login
                xmppHelper.login(
                        environment.getServerHost(),
                        environment.getServerPort(),
                        credentials.getUsername(),
                        credentials.getPassword());

                // save the user's credentials
                if(null == storedCredentials) {
                    createCredentials(
                            credentials.getUsername(), credentials.getPassword());
                }

                xmppHelper.processOfflineQueue();
            }
        } catch(final Throwable t) {
            if("No response from the server.".equals(t.getMessage())) {
                logWarning(MessageFormat.format(
                        "NO RESPONSE FROM SERVER:  {0}", attempt), t);
                login(attempt.intValue() + 1, environment, credentials);
            }
            throw translateError(t);
        }
    }
}
