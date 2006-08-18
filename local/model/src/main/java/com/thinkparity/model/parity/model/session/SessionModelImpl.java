/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.InputStream;
import java.util.*;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

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
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * The implementation of the parity session interface.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
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
    static void handleContactInvitationDeclined(final String invitedAs,
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
            final Long containerVersionId, final Integer count,
            final Integer index, final UUID uniqueId, final Long versionId,
            final String name, final ArtifactType type, final byte[] bytes)
            throws ParityException {
        final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        final Long containerId = artifactModel.readId(containerUniqueId);
        final Long id = artifactModel.readId(uniqueId);
        final InternalContainerModel containerModel = ContainerModel.getInternalModel(sContext);
        containerModel.handleArtifactPublished(publishedBy, publishedOn,
                containerUniqueId, containerId, containerVersionId, uniqueId,
                id, versionId, name, type, bytes);
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
            final Integer count, final Integer index, final UUID uniqueId,
            final Long versionId, final String name, final ArtifactType type,
            final byte[] bytes) throws ParityException {
        final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        final Long containerId = artifactModel.readId(containerUniqueId);
        final Long id = artifactModel.readId(uniqueId);
        final InternalContainerModel containerModel = ContainerModel.getInternalModel(sContext);
        containerModel.handleArtifactSent(sentBy, sentOn, containerUniqueId,
                containerId, containerVersionId, containerName, uniqueId, id,
                versionId, name, type, bytes);
    }

	/**
     * Create an incoming invitation for the user.
     * 
     * @param invitedBy
     *            From whome the invitation was extended.
     */
	static void handleInvitationExtended(final String invitedAs,
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
        debugVariable("contactId", contactId);
        synchronized (xmppHelper) {
            try {
                return xmppHelper.readContact(contactId);
            } catch (final Throwable t) {
                throw translateError("READ CONTACT", t);
            }
            
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
		synchronized(xmppHelper) {
			try { xmppHelper.acceptInvitation(invitedBy); }
			catch(final Throwable t) {
                throw translateError("[ACCEPT INVITATION]", t);
			}
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
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     */
    void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logger.info(getApiId("[ADD TEAM MEMBER]"));
        logger.debug(uniqueId);
        logger.debug(jabberId);
        synchronized(xmppHelper) {
            try { xmppHelper.addTeamMember(uniqueId, jabberId); }
            catch(final SmackException sx) {
                throw translateError(getApiId("[ADD TEAM MEMBER]"), sx);
            }
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
		debugVariable("uniqueId", uniqueId);
		synchronized (xmppHelper) {
			try { xmppHelper.createArtifact(uniqueId); }
            catch(final Throwable t) {
                throw translateError("[CREATE ARTIFACT]", t);
            }
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
	void declineInvitation(final String invitedAs, final JabberId invitedBy) {
		synchronized(xmppHelper) {
			try { xmppHelper.declineInvitation(invitedAs, invitedBy); }
			catch(final Throwable t) {
                throw translateError("[DECLINE INVITATION]", t);
			}
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
        debugVariable("uniqueId", uniqueId);
        synchronized (xmppHelper) {
            try { xmppHelper.deleteArtifact(uniqueId); }
            catch(final Throwable t) {
                throw translateError("[DELETE ARTIFACT]", t);
            }
        }
    }

	/**
	 * Invite a contact.
	 * 
	 * @param email
	 *            An e-mail address.
	 */
	void inviteContact(final String email) {
        logger.info(getApiId("[INVITE CONTACT]"));
        logger.debug(email);
        assertOnline("[INVITE CONTACT] [USER NOT ONLINE]");
		synchronized(xmppHelper) {
			try { xmppHelper.inviteContact(email); }
			catch(final SmackException sx) {
				throw translateError(getApiId("[INVITE CONTACT]"), sx);
			}
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
		final UUID uniqueId = readArtifactUniqueId(artifactId);
		synchronized(xmppHelper) {
			try {
				final JabberId keyHolder = xmppHelper.readKeyHolder(uniqueId);
				return keyHolder.equals(localUserId());
			} catch (final Throwable t) {
				throw translateError("[IS LOGGED IN USER KEY HOLDER]", t);
			}
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
        assertOnline(getApiId("[LOGOUT]"));
		synchronized(xmppHelper) {
			try { xmppHelper.logout(); }
			catch(final SmackException sx) {
				throw translateError(getApiId("[LOGOUT]"), sx);
			}
		}
	}

	/**
     * Send a container to a list of users.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents.
     * @param users
     *            A list of users.
     */
    void publish(final ContainerVersion container,
            final List<TeamMember> teamMembers,
            final Map<DocumentVersion, InputStream> documents,
            final JabberId publishedBy, final Calendar publishedOn) {
        logApiId();
        debugVariable("container", container);
        debugVariable("teamMembers", teamMembers);
        debugVariable("documents", documents);
        debugVariable("publishedBy", publishedBy);
        debugVariable("publishedOn", publishedOn);
        synchronized (xmppHelper) {
            try {
                xmppHelper.publish(container, teamMembers, documents,
                        publishedBy, publishedOn);
            } catch(final Throwable t) {
                throw translateError("PUBLISH", t);
            }
        }
    }

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            The artifact id.
     * @return A set of users.
     * @throws ParityException
     */
	List<User> readArtifactTeam(final Long artifactId)
            throws ParityException {
		logger.info("[LMODEL] [SESSION] [READ ARTIFACT TEAM]");
		logger.debug(artifactId);
		synchronized(xmppHelper) {
			assertIsLoggedIn("[LMODEL] [SESSION] [READ ARTIFACT TEAM]",
					xmppHelper);
			try {
				final UUID uniqueId = readArtifactUniqueId(artifactId);
				return xmppHelper.readArtifactTeam(uniqueId);
			}
			catch(final SmackException sx) {
				logger.error("[LMODEL] [SESSION] [READ ARTIFACT TEAM] [XMPP ERROR]", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("[LMODEL] [SESSION] [READ ARTIFACT TEAM] [UNKNOWN ERROR]", rx);
				throw ParityErrorTranslator.translate(rx);
			}
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
		synchronized(xmppHelper) {
			try { return xmppHelper.readContacts(); }
			catch(final Throwable t) {
                throw translateError("[READ CONTACTS]", t);
			}
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
        debugVariable("uniqueId", uniqueId);
		synchronized (xmppHelper) {
			try {
                return xmppHelper.readKeyHolder(uniqueId);
			} catch (final Throwable t) {
				throw translateError("[READ ARTIFACT KEY HOLDER]", t);
			}
		}
	}

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    Profile readProfile() {
        logger.info(getApiId("[READ PROFILE]"));
        assertOnline(getApiId("[READ PROFILE] [USER NOT ONLINE]"));
        synchronized(xmppHelper) {
            try { return xmppHelper.readProfile(); }
            catch(final SmackException sx) {
                throw translateError(getApiId("[READ PROFILE]"), sx);
            }
        }
    }

    /**
     * Read the logged in user's session.
     * 
     * @return The logged in user's session.
     */
    Session readSession() {
        logger.info(getApiId("[READ SESSION]"));
        assertOnline(getApiId("[READ SESSION] [USER NOT ONLINE]"));
        final Session session = new Session();
        try { session.setJabberId(xmppHelper.getUser().getId()); }
        catch(final SmackException sx) {
            throw translateError(getApiId("[READ SESSION]"), sx);
        }
        return session;
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
>>>>>>> 1.7.2.57
     * Read a single user.
     * 
     * @param jabberId
     *            The user id.
     * @return The user.
     * @throws ParityException
     */
	User readUser(final JabberId jabberId) {
		final Set<JabberId> jabberIds = new HashSet<JabberId>();
		jabberIds.add(jabberId);
		return readUsers(jabberIds).iterator().next();
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
		synchronized(xmppHelper) {
			try { return xmppHelper.readUsers(jabberIds); }
			catch(final Throwable t) {
				throw translateError("[READ USERS]", t);
			}
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
    void send(final ContainerVersion version,
            final Map<DocumentVersion, InputStream> documentVersions,
            final User user, final JabberId sentBy, final Calendar sentOn) {
        logger.info(getApiId("[SEND]"));
        logger.debug(version);
        logger.debug(documentVersions);
        logger.debug(user);
        synchronized(xmppHelper) {
            try { xmppHelper.send(version, documentVersions, user, sentBy, sentOn); }
            catch(final SmackException sx) {
                throw translateError(getApiId("[SEND]"), sx);
            }
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
     * Update the user.
     * 
     * @param user
     *            The user.
     * @throws ParityException
     */
    void updateUser(final User user) throws ParityException {
        logger.info(getApiId("[UPDATE USER]"));
        logger.debug(user);
        synchronized(SessionModelImpl.xmppHelper) {
            assertIsLoggedIn(getApiId("[UPDATE USER]"), xmppHelper);
            try { xmppHelper.updateUser(user); }
            catch(final SmackException sx) {
                logger.error(getErrorId("[UPDATE USER]", "[SMACK ERROR]"), sx);
                throw ParityErrorTranslator.translate(sx);
            }
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
     * Assert that the user is currently logged in.
     * 
     * @param message
     *            Message to display in the assertion.
     * @param xmppHelper
     *            A handle to the xmpp helper in order to determine logged in
     *            status.
     */
    private void assertIsLoggedIn(final StringBuffer message,
            final SessionModelXMPPHelper xmppHelper) {
        assertIsLoggedIn(message.toString(), xmppHelper);
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
        assertNotIsOnline("USER ALREADY ONLINE");
        assertIsReachable("ENVIRONMENT NOT REACHABLE", environment);
        synchronized(xmppHelper) {
            try {
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
            catch(final SmackException sx) {
                throw translateError(getApiId("[LOGIN]"), sx);
            }
        }        
    }
}
