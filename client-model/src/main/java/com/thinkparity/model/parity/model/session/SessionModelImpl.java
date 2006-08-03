/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.*;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.KeyEvent;
import com.thinkparity.model.parity.api.events.KeyListener;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.container.InternalContainerModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.profile.Profile;
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
	 * Static handle to the session model's auditor.
	 * 
	 */
	private static final SessionModelAuditor sAuditor;

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
		sAuditor = new SessionModelAuditor(sContext);
		// create the xmpp helper
		xmppHelper = new SessionModelXMPPHelper();
	}

	/**
     * Handle the container reactivation.
     * 
     * @param uniqueId
     *            The unique id.
     * @param versionId
     *            The version id.
     * @param name
     *            The name.
     * @param team
     *            The team.
     * @param reactivatedBy
     *            Who reactivated the container.
     * @param reactivatedOn
     *            When the container was reactivated.
     * @throws ParityException
     */
    static void handleContainerReactivate(final UUID uniqueId,
            final Long versionId, final String name, final List<JabberId> team,
            final JabberId reactivatedBy, final Calendar reactivatedOn)
            throws ParityException {
        Assert.assertNotYetImplemented("SessionModelImpl#handleContainerReactivate");
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
            final JabberId jabberId) throws ParityException {
	    final InternalArtifactModel artifactModel = ArtifactModel.getInternalModel(sContext);
        artifactModel.handleTeamMemberAdded(uniqueId, jabberId);
	}

	/**
     * Handle the event generated by the server to close an artifact.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @param artifactClosedBy
     *            The user closing the document.
     * @throws ParityException
     */
	static void notifyArtifactClosed(final UUID artifactUniqueId,
			final JabberId artifactClosedBy) throws ParityException {
        final InternalArtifactModel aModel = ArtifactModel.getInternalModel(sContext);
        final Long artifactId = aModel.readId(artifactUniqueId);

        final InternalContainerModel cModel = ContainerModel.getInternalModel(sContext);
        cModel.handleClose(artifactId, artifactClosedBy, currentDateTime());
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
	 * Handle the event generated by xmppExtensionListenerImpl.  Here we create
	 * a new document based upon the document version.
	 * 
	 * @param xmppDocument
	 *            The xmpp document that has been received.
	 */
	static void notifyDocumentReceived(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] content) throws ParityException, SmackException {
		DocumentModel.getInternalModel(sContext).receive(receivedFrom, uniqueId,
                versionId, name, content);
	}

    /**
	 * @deprecated The accept\decline contact notifications are
	 * no longer required.
	 */
	static void notifyInvitationAccepted(final JabberId acceptedBy) {
		SystemMessageModel.getInternalModel(sContext).createContactInvitationResponse(acceptedBy, Boolean.TRUE);
	}

	/**
	 * @deprecated The accept\decline contact notifications are
	 * no longer required.
	 */
	static void notifyInvitationDeclined(final JabberId declinedBy) {
		SystemMessageModel.getInternalModel(sContext).createContactInvitationResponse(declinedBy, Boolean.FALSE);
	}

	static void notifyInvitationExtended(final JabberId invitedBy) {
		SystemMessageModel.getInternalModel(sContext).createContactInvitation(invitedBy);
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
	 * Notify all of the registered key listeners that a user has requested the
	 * key for a given artifact.
	 * 
	 * @param user
	 *            The requesting user.
	 * @param artifactUUID
	 *            The artifact.
	 */
	static void notifyKeyRequested(final UUID artifactUniqueId,
			final JabberId requestedBy) throws ParityException, SmackException {
		final Calendar currentDateTime = currentDateTime();
		final InternalDocumentModel iDModel = DocumentModel.getInternalModel(sContext);
		final Document document = iDModel.get(artifactUniqueId);

		// request key
		iDModel.requestKey(document.getId(), requestedBy);

		// update the remote info row
		final InternalArtifactModel iAModel =
			ArtifactModel.getInternalModel(sContext);
		iAModel.updateRemoteInfo(document.getId(), requestedBy, currentDateTime);

		// audit key request
		final User loggedInUser;
		synchronized(xmppHelper) { loggedInUser =  xmppHelper.getUser(); }
		sAuditor.requestKey(document.getId(), loggedInUser.getId(), currentDateTime, requestedBy, loggedInUser.getId());

		// fire the key requested event
		notifyKey_keyRequested(document.getId());
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

	/**
     * Handle the event that a new team member was removed from the artifact.
     * 
     * @param artifactUniqueId
     *            The artifact's unique id.
     * @param newTeamMember
     *            The team member.
     * @throws ParityException
     */
	static void notifyTeamMemberRemoved(final UUID uniqueId,
			final User teamMember) throws ParityException {
	    Assert.assertNotYetImplemented("SessionModel#notifyTeamMemberRemoved(UUID,User)");
	}

	private static StringBuffer getApiId(final String api) {
        return getModelId("SESSION").append(" ").append(api);
    }

	private static StringBuffer getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error);
    }

	/**
	 * Fire the keyRequestDeclined event for all key listeners.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	private static void notifyKey_keyRequested(final Long artifactId) {
		synchronized(keyListeners) {
			final KeyEvent e = new KeyEvent(artifactId);
			for(final KeyListener l : keyListeners) { l.keyRequested(e); }
		}
	}

    /**
	 * The session model auditor.
	 * 
	 */
	private final SessionModelAuditor auditor;

	/**
	 * Create a SessionModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	SessionModelImpl(final Workspace workspace) {
		super(workspace);
		this.auditor = new SessionModelAuditor(getContext());
	}

	/**
	 * Accept an invitation to the user's contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	void acceptInvitation(final JabberId jabberId) throws ParityException {
		synchronized(xmppHelper) {
			try { xmppHelper.acceptInvitation(jabberId); }
			catch(SmackException sx) {
				logger.error("acceptPresence(User)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("acceptPresence(User)", rx);
				throw ParityErrorTranslator.translate(rx);
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
                logger.error(getErrorId("[ADD TEAM MEMBER]", "[COULD NOT ADD TEAM MEMBER]"), sx);
                throw ParityErrorTranslator.translateUnchecked(sx);
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
	void declineInvitation(final JabberId jabberId) throws ParityException {
		synchronized(xmppHelper) {
			assertIsLoggedIn("Cannot decline invitation while offline.", xmppHelper);
			try { xmppHelper.declineInvitation(jabberId); }
			catch(SmackException sx) {
				logger.error("Could not decline invitation:  " + jabberId, sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("Could not decline invitation:  " + jabberId, rx);
				throw ParityErrorTranslator.translate(rx);
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
				logger.error(getApiId("[INVITE CONTACT]"), sx);
				throw ParityErrorTranslator.translateUnchecked(sx);
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
	Boolean isLoggedInUserKeyHolder(final Long artifactId) throws ParityException {
		logger.info("isLoggedInUserKeyHolder(Long)");
		logger.debug(artifactId);
		final UUID artifactUniqueId = readArtifactUniqueId(artifactId);
		synchronized(xmppHelper) {
			assertIsLoggedIn(
					"Cannot determine whether the logged in user is the key holder while offline.",
					xmppHelper);
			final User loggedInUser = readUser();
			try {
				final User keyHolder =
					xmppHelper.getArtifactKeyHolder(artifactUniqueId);
				return keyHolder.getSimpleUsername().equals(loggedInUser.getSimpleUsername());
			}
			catch(final SmackException sx) {
				logger.error("Cannot determine whether the logged in user is the key holder.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot determine whether the logged in user is the key holder.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
     * Establish a new xmpp session.
     * 
     * @throws ParityException
     */
    void login() throws ParityException {
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
	void login(final Credentials credentials) throws ParityException {
		logger.info(getApiId("[LOGIN]"));
		logger.debug(credentials);
        assertNotIsOnline(getErrorId("[LOGIN]", "[USER ALREADY ONLINE]").toString());
		final String host = preferences.getServerHost();
		final Integer port = preferences.getServerPort();
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
				xmppHelper.login(host, port, credentials.getUsername(), credentials.getPassword());

                // save the user's credentials
				if(null == storedCredentials) {
                    createCredentials(
                            credentials.getUsername(), credentials.getPassword());
                }

				xmppHelper.processOfflineQueue();
			}
			catch(final SmackException sx) {
				logger.error("[LMODEL] [SESSION] [LOGIN] [XMPP ERROR])", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("[LMODEL] [SESSION] [LOGIN] [UNKNOWN ERROR])", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Terminate the current session.
	 * 
	 * @throws ParityException
	 */
	void logout() throws ParityException {
		synchronized(xmppHelper) {
			assertIsLoggedIn("logout", xmppHelper);
			try { xmppHelper.logout(); }
			catch(SmackException sx) {
				logger.error("logout()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("logout()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

    /**
     * Reactivate a container version.
     * 
     * @param version
     *            The version.
     * @param documentVersions
     *            The document versions.
     * @param team
     *            The team.
     * @param reactivatedBy
     *            Who reactivated.
     * @param reactivatedOn
     *            When reactivated.
     * @throws ParityException
     */

    void reactivate(final ContainerVersion version,
            final List<DocumentVersionContent> documentVersions,
            final List<JabberId> team, final JabberId reactivatedBy,
            final Calendar reactivatedOn) throws ParityException {
        logger.info(getApiId("[SEND REACTIVATE]"));
        logger.debug(version);
        logger.debug(team);
        logger.debug(reactivatedBy);
        logger.debug(reactivatedOn);
        assertOnline(getApiId("[REACTIVATE] [USER NOT ONLINE]"));
        synchronized(xmppHelper) {
            try {
                xmppHelper.reactivate(version, documentVersions, team,
                        reactivatedBy, reactivatedOn);
            }
            catch(final SmackException sx) {
                logger.error(getApiId("[REACTIVATE]"), sx);
                throw ParityErrorTranslator.translate(sx);
            }
            catch(final RuntimeException rx) {
                logger.error(getApiId("[REACTIVATE]"), rx);
                throw ParityErrorTranslator.translate(rx);
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
    JabberId readArtifactKeyHolder(final UUID artifactUniqueId)
            throws ParityException {
		logger.info(getApiId("[READ ARTIFACT KEY HOLDER]"));
		logger.debug(artifactUniqueId);
        assertOnline(getApiId("[READ ARTIFACT KEY HOLDER] [USER NOT ONLINE]"));
		synchronized(xmppHelper) {
			try {
				return xmppHelper.getArtifactKeyHolder(artifactUniqueId).getId();
			}
			catch(final SmackException sx) {
				logger.error(getApiId("[READ ARTIFACT KEY HOLDER] [SMACK ERROR]"), sx);
				throw ParityErrorTranslator.translate(sx);
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
	List<Contact> readContacts() throws ParityException {
		synchronized(xmppHelper) {
			assertIsLoggedIn(getApiId("[READ CONTACTS] [USER NOT ONLINE]"), xmppHelper);
			try { return xmppHelper.readContacts(); }
			catch(final SmackException sx) {
				logger.error(getApiId("[READ CONTACTS] [XMPP ERROR]"), sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
                logger.error(getApiId("[READ CONTACTS] [UNKNOWN ERROR]"), rx);
				throw ParityErrorTranslator.translate(rx);
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
                throw ParityErrorTranslator.translateUnchecked(sx);
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
            throw ParityErrorTranslator.translateUnchecked(sx);
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
	User readUser(final JabberId jabberId) throws ParityException {
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
	Set<User> readUsers(final Set<JabberId> jabberIds) throws ParityException {
		synchronized(xmppHelper) {
			assertIsLoggedIn("Cannot read user list while offline.", xmppHelper);
			try { return xmppHelper.readUsers(jabberIds); }
			catch(final SmackException sx) {
				logger.error("Cannot read users.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot read users.", rx);
				throw ParityErrorTranslator.translate(rx);
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
	 * Send a particular revision to a list of users. The version is obtained
	 * from the document model; and streamed to the list of users.
	 * 
	 * @param users
	 *            The list of users to send the document version to.
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 * @throws ParityException
	 */
	void send(final Collection<User> users, final Long documentId,
			final Long versionId) throws ParityException {
		logger.info("send(Collection<User>,Long,Long)");
		logger.debug(users);
		logger.debug(documentId);
		logger.debug(versionId);
		synchronized(xmppHelper) {
			try {
				final InternalDocumentModel iDModel = getInternalDocumentModel();
                final Document d = iDModel.get(documentId);
				final DocumentVersion dv = iDModel.getVersion(documentId, versionId);
				final DocumentVersionContent vc = iDModel.getVersionContent(documentId, versionId);

				// send the document version
				xmppHelper.sendDocumentVersion(extractIdSet(users),
                        d.getUniqueId(), dv.getVersionId(), d.getName(),
                        vc.getContent());

				// audit the send
                final Calendar currentDateTime = currentDateTime();
				auditor.send(dv.getArtifactId(), currentDateTime,
                        currentUserId(), dv.getVersionId(), currentUserId(),
                        currentDateTime, users);
			}
			catch(final SmackException sx) {
				logger.error("Could not send document version.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Could not send document version.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send a close packet to the server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 */
	void sendClose(final Long artifactId) throws ParityException {
		logger.info("[LMODEL] [SESSION] [SEND CLOSE]");
		logger.debug(artifactId);
        assertOnline("[LMODEL] [SESSION] [SEND CLOSE] [USER IS NOT ONLINE]");
        assertIsKeyHolder(
                "[LMODEL] [SESSION] [SEND CLOSE] [USER IS NOT KEYHOLDER]", artifactId);
		synchronized(xmppHelper) {
			try {
				final UUID artifactUniqueId = readArtifactUniqueId(artifactId);
				xmppHelper.sendClose(artifactUniqueId);
			}
			catch(final SmackException sx) {
				logger.error("Cannot call remote close api.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Cannot close artifact:  " + artifactId, rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

    /**
	 * Send an artifact creation packet to the parity server.
	 * 
	 * @param artifact
	 *            The document.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	void sendCreate(final Artifact artifact) throws ParityException {
		logger.info("sendCreate(Artifact)");
		logger.debug(artifact);
		synchronized(SessionModelImpl.xmppHelper) {
			assertIsLoggedIn("sendCreate(Artifact)", SessionModelImpl.xmppHelper);
			try { SessionModelImpl.xmppHelper.sendCreate(artifact.getUniqueId()); }
			catch(SmackException sx) {
				logger.error("sendCreate(Artifact)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("sendCreate(Artifact)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Send a delete packet to the parity server.  Note that sendDelete is a
     * misnomer.  It deletes the current user's subscription adn only flags the
     * remote document as deleted once all subscriptions have been removed.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user if offline.
	 *             </ul>
	 */
	void sendDelete(final Long artifactId) throws ParityException {
		logger.info("sendDelete(Long)");
		logger.debug(artifactId);
		synchronized(SessionModelImpl.xmppHelper) {
			assertIsLoggedIn("Must be online to delete.", xmppHelper);
			try {
				final UUID artifactUniqueId = readArtifactUniqueId(artifactId);
				xmppHelper.sendDelete(artifactUniqueId);
			}
			catch(final SmackException sx) {
				logger.error("Could not delete artifact:  " + artifactId, sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("Could not delete artifact:  " + artifactId, rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	void sendDocumentReactivate(final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] bytes)
            throws ParityException {
        synchronized(SessionModelImpl.xmppHelper) {
            assertIsLoggedIn("[SESSION MODEL] [SEND DOCUMENT REACTIVATE] [USER NOT LOGGED IN]", SessionModelImpl.xmppHelper);
            try { xmppHelper.sendDocumentReactivate(team, uniqueId, versionId, name, bytes); }
            catch(final SmackException sx) {
                logger.error("[SESSION MODEL] [SEND DOCUMENT REACTIVATE] [UNKNOWN ERROR]", sx);
                throw ParityErrorTranslator.translate(sx);
            }
        }
    }

    /**
	 * Send the response to a key request. It is assumed that the logged in user
	 * is the key holder. It is assumed that all key information is stored on
	 * the parity server and never locally. It is assumed that they key will be
	 * accompanied by the working document.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param requestedBy
	 *            The jabber id of the user requesting the key.
	 * @param keyResponse
	 *            The response.
	 */
	void sendKey(final UUID artifactUniqueId, final JabberId jabberId)
            throws ParityException {
		logger.info(getApiId("[SEND KEY]"));
		logger.debug(artifactUniqueId);
        logger.debug(jabberId);
        assertOnline(getApiId("[SEND KEY] [USER NOT ONLINE]"));
        synchronized(xmppHelper) {
            try {
                xmppHelper.sendKeyResponse(artifactUniqueId, KeyResponse.ACCEPT, jabberId);
            }
            catch(final SmackException sx) {
                logger.error(getApiId("[SEND KEY] [SMACK ERROR]"), sx);
                throw ParityErrorTranslator.translate(sx);
            }
        }
	}

    /**
	 * Send a reqest for a document key to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @throws ParityException
	 * @see KeyListener#keyRequested(KeyEvent)
	 */
	void sendKeyRequest(final Long artifactId) throws ParityException {
		logger.info("sendKeyRequest(Long)");
		logger.debug(artifactId);
		synchronized(SessionModelImpl.xmppHelper) {
			assertIsLoggedIn("Cannot request key while offline.", xmppHelper);
			try {
				final UUID artifactUniqueId = readArtifactUniqueId(artifactId);
				xmppHelper.sendKeyRequest(artifactUniqueId);

				// audit key request
				final JabberId currentUserId = currentUserId();
				final JabberId keyHolder = readArtifactKeyHolder(artifactUniqueId);
				auditor.requestKey(artifactId, currentUserId, currentDateTime(),
                        currentUserId, keyHolder);
			}
			catch(SmackException sx) {
				logger.error("Cannot send key request.", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("Cannot send key request.", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

    /**
     * Send a key response [ACCEPT,DENY] to a user.
     * 
     * @param keyResponse
     *            The key response.
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws ParityException
     */
    void sendKeyResponse(final KeyResponse keyResponse, final Long artifactId,
            final JabberId jabberId) throws ParityException {
        logger.info(getApiId("[SEND KEY RESPONSE]"));
        logger.debug(keyResponse);
        logger.debug(artifactId);
        logger.debug(jabberId);
        synchronized(xmppHelper) {
            final UUID artifactUniqueId = readArtifactUniqueId(artifactId);
            try {
                xmppHelper.sendKeyResponse(artifactUniqueId, keyResponse, jabberId);
            }
            catch(final SmackException sx) {
                logger.error(getApiId("[SEND KEY RESPONSE] [SMACK ERROR]"), sx);
                throw ParityErrorTranslator.translate(sx);
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
		synchronized(xmppHelper) {
			assertIsLoggedIn("sendLogFile()", xmppHelper);
			try {
				xmppHelper.sendLogFileArchive(
						workspace.getLogArchive(), preferences.getSystemUser());
			}
			catch(final SmackException sx) {
				logger.error("sendLogFile()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(final RuntimeException rx) {
				logger.error("sendLogFile()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
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
     * Extract a set of jabber ids from the list of users.
     * 
     * @param users
     *            A list of users.
     * @return A set of jabber ids.
     */
    private Set<JabberId> extractIdSet(final Iterable<User> users) {
        final Set<JabberId> jabberIds = new HashSet<JabberId>();
        for(final User user : users) { jabberIds.add(user.getId()); }
        return jabberIds;
    }
}
