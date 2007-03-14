/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.40
 */
public final class SessionModelImpl extends Model<SessionListener>
        implements SessionModel, InternalSessionModel {

    /**
	 * Create a SessionModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	public SessionModelImpl() {
		super();
	}

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#acceptIncomingEMailInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation, java.util.Calendar)
     *
     */
    public void acceptInvitation(final IncomingEMailInvitation invitation,
            final Calendar acceptedOn) {
       try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.acceptInvitation(localUserId(), invitation,
                        acceptedOn);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#acceptIncomingUserInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation, java.util.Calendar)
     *
     */
    public void acceptInvitation(final IncomingUserInvitation invitation,
            final Calendar acceptedOn) {
       try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.acceptInvitation(localUserId(), invitation,
                        acceptedOn);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#addListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    public void addListener(final SessionListener listener) {
        super.addListener(listener);
    }

    /**
     * Add an email to a user's profile.
     * 
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void addProfileEmail(final JabberId userId, final ProfileEMail email) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.addProfileEmail(userId, email.getEmail());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#addTeamMember(java.util.UUID,
     *      java.util.List, com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void addTeamMember(final UUID uniqueId, final List<JabberId> team,
            final JabberId teamMemberId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("team", team);
        logger.logVariable("teamMemberId", teamMemberId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.addTeamMember(localUserId(), team, uniqueId,
                        teamMemberId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#archiveArtifact(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public void archiveArtifact(final JabberId userId, final UUID uniqueId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.archiveArtifact(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public void confirmArtifactReceipt(final JabberId userId, final UUID uniqueId,
            final Long versionId, final JabberId publishedBy,
            final Calendar publishedOn, final List<JabberId> publishedTo,
            final JabberId receivedBy, final Calendar receivedOn) {
	    logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        logger.logVariable("publishedTo", publishedTo);
        logger.logVariable("receivedBy", receivedBy);
        logger.logVariable("receivedOn", receivedOn);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
    	    synchronized(xmppSession) {
    	        xmppSession.confirmArtifactReceipt(localUserId(), uniqueId,
                        versionId, publishedBy, publishedOn, publishedTo,
                        receivedBy, receivedOn);
    	    }
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * Send an artifact creation packet to the parity server.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void createArtifact(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn) {
		logger.logApiId();
        logger.logVariable("userId", userId);
		logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("createdOn", createdOn);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        xmppSession.createArtifact(userId, uniqueId, createdOn);
		    }
		} catch (final Throwable t) {
            throw translateError(t);
        }
	}

    public void createBackupStream(final JabberId userId, final String streamId,
            final UUID uniqueId, final Long versionId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.createBackupStream(userId, streamId, uniqueId, versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#createDraft(java.util.List,
     *      java.util.UUID, java.util.Calendar)
     * 
     */
    public void createDraft(final List<JabberId> team, final UUID uniqueId,
            final Calendar createdOn) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.createDraft(localUserId(), team, uniqueId,
                        createdOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#createInvitation(com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void createInvitation(final OutgoingEMailInvitation invitation) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.createInvitation(localUserId(), invitation);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#createInvitation(com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     * 
     */
    public void createInvitation(final OutgoingUserInvitation invitation) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.createInvitation(localUserId(), invitation);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#createMigratorStream(java.lang.String, java.util.List)
     *
     */
    public void createMigratorStream(final String streamId,
            final List<Resource> resources) {
        logger.logApiId();
        logger.logVariable("streamId", streamId);
        logger.logVariable("resources", resources);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.createMigratorStream(localUserId(),
                        streamId, resources);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public String createStream(final StreamSession session) {
        logger.logApiId();
        logger.logVariable("session", session);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.createStream(localUserId(), session);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a stream session.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createStreamSession() {
        logger.logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.createStreamSession(localUserId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#declineInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation, java.util.Calendar)
     *
     */
    public void declineInvitation(final IncomingEMailInvitation invitation,
            final Calendar declinedOn) {
       try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.declineInvitation(localUserId(), invitation,
                        declinedOn);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#declineInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation, java.util.Calendar)
     *
     */
    public void declineInvitation(final IncomingUserInvitation invitation,
            final Calendar declinedOn) {
       try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.declineInvitation(localUserId(), invitation,
                        declinedOn);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#deleteContact(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void delete(final JabberId contactId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.delete(localUserId(), contactId);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#deleteArtifact(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     *
     */
    public void deleteArtifact(final JabberId userId, final UUID uniqueId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteArtifact(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#deleteDraft(java.util.UUID,
     *      java.util.Calendar)
     * 
     */
    public void deleteDraft(final UUID uniqueId, final Calendar deletedOn) {
        try {
            final InternalArtifactModel artifactModel = getArtifactModel();
            final Long artifactId = artifactModel.readId(uniqueId);
            final List<JabberId> team = artifactModel.readTeamIds(artifactId);
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteDraft(localUserId(), team, uniqueId, deletedOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#deleteInvitation(com.thinkparity.codebase.model.contact.OutgoingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation,
            final Calendar deletedOn) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteInvitation(localUserId(), invitation,
                        deletedOn);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#deleteInvitation(com.thinkparity.codebase.model.contact.OutgoingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation,
            final Calendar deletedOn) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteInvitation(localUserId(), invitation,
                        deletedOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public void deleteStreamSession(final StreamSession session) {
        logger.logApiId();
        logger.logVariable("session", session);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteStreamSession(localUserId(), session);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#deployMigratorRelease(java.util.UUID)
     *
     */
    public void deployMigrator(final Product product,
            final Release release, final List<Resource> resources,
            final String streamId) {
        try {
            assertOnline();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deployMigrator(localUserId(), product, release,
                        resources, streamId);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Handle the session established remote event.
     *
     */
    public void handleSessionEstablished() {
        logger.logApiId();
        try {
            notifyListeners(new EventNotifier<SessionListener>() {
                public void notifyListener(final SessionListener listener) {
                    listener.sessionEstablished();
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the remote session terminated event.
     *
     */
    public void handleSessionTerminated() {
        logger.logApiId();
        try {
            notifyListeners(new EventNotifier<SessionListener>() {
                public void notifyListener(final SessionListener listener) {
                    listener.sessionTerminated();
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Handle the remote session terminated event.
     * 
     * @param cause
     *            The cause of the termination.
     * 
     */
    public void handleSessionTerminated(final Exception cause) {
        logger.logApiId();
        try {
            notifyListeners(new EventNotifier<SessionListener>() {
                public void notifyListener(final SessionListener listener) {
                    listener.sessionTerminated(cause);
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#isBackupOnline()
     *
     */
    public Boolean isBackupOnline() {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.isBackupOnline(localUserId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#isEmailAvailable(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public Boolean isEmailAvailable(final JabberId userId, final EMail email) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.isEmailAvailable(userId, email);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
	 * Determine whether or not a user is logged in.
	 * 
	 * @return True if the user is logged in, false otherwise.
	 */
    public Boolean isLoggedIn() {
        logger.logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
    		synchronized (xmppSession) {
                return xmppSession.isLoggedIn();
    		}
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

	/**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#isPublishRestricted(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Boolean isPublishRestricted(final JabberId publishTo) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.isPublishRestricted(localUserId(),
                        localUserId(), publishTo);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#login(com.thinkparity.codebase.model.session.Credentials)
     *
     */
    public void login(final Credentials credentials)
            throws InvalidCredentialsException {
        logger.logVariable("environment", environment);
        logger.logVariable("credentials", credentials);
        try {
            assertNotIsOnline();
            assertXMPPIsReachable(environment);
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                // register xmpp event listeners
                new SessionModelEventDispatcher(workspace, modelFactory, xmppSession);
                // login
                try {
                    xmppSession.login(environment, credentials);
                } catch (final InvalidCredentialsException icx) {
                    throw icx;
                }
                // this was the first login
                createCredentials(credentials);
                createToken(xmppSession.createToken(localUserId()));
            }
        } catch (final InvalidCredentialsException icx) {
            throw icx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.SessionModel#login(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    public void login(final ProcessMonitor monitor)
            throws InvalidCredentialsException, InvalidLocationException {
        try {
            assertNotIsOnline();
            assertXMPPIsReachable(environment);
            notifyProcessBegin(monitor);
            final Credentials credentials = readCredentials();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                // check that the credentials match
                final Credentials localCredentials = readCredentials();
                Assert.assertTrue(
                        localCredentials.getUsername().equals(credentials.getUsername()) &&
                        localCredentials.getPassword().equals(credentials.getPassword()),
                        "Credentials {0} do not match local credentials {1}.",
                        credentials, localCredentials);
                credentials.setResource(localCredentials.getResource());
                // register xmpp event listeners
                new SessionModelEventDispatcher(workspace, modelFactory, xmppSession);
                // login
                try {
                    xmppSession.login(environment, credentials);
                } catch (final InvalidCredentialsException icx) {
                    throw icx;
                }
                // ensure environment integrity
                final Token localToken = readToken();
                final Token remoteToken = xmppSession.readToken(localUserId());
                if (localToken.equals(remoteToken)) {
                    // process queued events
                    xmppSession.processEventQueue(monitor, localUserId());
                    xmppSession.registerQueueListener();
                } else {
                    logger.logError("Cannot login from more than one location.");
                    xmppSession.logout();
                    throw new InvalidLocationException();
                }
            }
        } catch (final InvalidCredentialsException icx) {
            throw icx;
        } catch (final InvalidLocationException ilx) {
            throw ilx;
        } catch (final Throwable t) {
            throw translateError(t);
        } finally {
            notifyProcessEnd(monitor);
        }
    }

	/**
	 * Terminate the current session.
	 * 
	 */
    public void logout() {
        logger.logApiId();
		try {
            logger.logTraceId();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            logger.logTraceId();
            synchronized (xmppSession) {
                logger.logTraceId();
                xmppSession.logout();
                logger.logTraceId();
            }
            logger.logTraceId();
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#processQueue(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    public void processQueue(final ProcessMonitor monitor) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.processEventQueue(monitor, localUserId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#publish(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.List,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar,
     *      java.util.List)
     * 
     */
    public void publish(final ContainerVersion container,
            final Map<DocumentVersion, String> documents,
            final List<TeamMember> teamMembers, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishedTo) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.publish(localUserId(), container, documents,
                        teamMembers, publishedBy, publishedOn, publishedTo);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    public Container readArchiveContainer(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveContainer(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A list of conatiners.
     */
    public List<Container> readArchiveContainers(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveContainers(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readArchiveContainerVersions(
            final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveContainerVersions(userId,
                        uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    public List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocuments(userId, uniqueId,
                        versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public DocumentVersion readArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final UUID documentUniqueId,
            final Long documentVersionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("documentVersionId", documentVersionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocumentVersion(userId, uniqueId,
                        documentUniqueId, documentVersionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(final JabberId userId,
            final UUID uniqueId, final Long compareVersionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("compareVersionId", compareVersionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocumentVersionDeltas(userId,
                        uniqueId, compareVersionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
    
    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("compareVersionId", compareVersionId);
        logger.logVariable("compareToVersionId", compareToVersionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocumentVersionDeltas(userId,
                        uniqueId, compareVersionId, compareToVersionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
    
    /**
     * Read the archived document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readArchiveDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocumentVersions(userId,
                        uniqueId, versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public List<TeamMember> readArchiveTeam(final JabberId userId,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveTeam(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archive team.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of jabber ids.
     */
    public List<JabberId> readArchiveTeamIds(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveTeamIds(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public Container readBackupContainer(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupContainer(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readBackupContainers(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupContainers(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readBackupContainerVersions(final JabberId userId,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupContainerVersions(userId,
                        uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    public List<Document> readBackupDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupDocuments(userId, uniqueId,
                        versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readBackupDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupDocumentVersions(userId,
                        uniqueId, versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public List<ArtifactReceipt> readBackupPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupPublishedTo(userId, uniqueId,
                        versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup team.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of jabber ids.
     */
    public List<JabberId> readBackupTeamIds(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readBackupTeamIds(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readContact(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Contact readContact(final JabberId contactId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readContact(localUserId(), contactId);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readContactIds()
     *
     */
    public List<JabberId> readContactIds() {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized(xmppSession) {
                return xmppSession.readContactIds(localUserId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readDateTime()
     * 
     */
    public Calendar readDateTime() {
        logger.logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readDateTime(localUserId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readIncomingEMailInvitations()
     *
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations() {
       try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readIncomingEMailInvitations(localUserId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readIncomingUserInvitations()
     *
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations() {
       try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readIncomingUserInvitations(localUserId());
            }
        } catch (final Throwable t) {
            throw panic(t);
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
    public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
		logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        return xmppSession.readKeyHolder(userId, uniqueId);
		    }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

	/**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readMigratorLatestRelease(java.util.UUID, com.thinkparity.codebase.OS)
     *
     */
    public Release readMigratorLatestRelease(final UUID productUniqueId, final OS os) {
        try {
            assertOnline();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readMigratorLatestRelease(localUserId(),
                        productUniqueId, os);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readMigratorProduct(java.lang.String)
     *
     */
    public Product readMigratorProduct(final String name) {
        try {
            assertOnline();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readMigratorProduct(localUserId(), name);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    
    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readMigratorRelease(java.lang.String)
     *
     */
    public Release readMigratorRelease(final UUID productUniqueId,
            final String name, final OS os) {
        try {
            assertOnline();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readMigratorRelease(localUserId(),
                        productUniqueId, name, os);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }
    
    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readMigratorResources(java.util.UUID, java.lang.String, com.thinkparity.codebase.OS)
     *
     */
    public List<Resource> readMigratorResources(final UUID productUniqueId,
            final String releaseName, final OS os) {
        try {
            assertOnline();
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readMigratorResources(localUserId(),
                        productUniqueId, releaseName, os);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readOutgoingEMailInvitations()
     *
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readOutgoingEMailInvitations(localUserId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#readOutgoingUserInvitations()
     *
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readOutgoingUserInvitations(localUserId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

	/**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile() {
        logger.logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readProfile();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public List<EMail> readProfileEMails() {
        logger.logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readProfileEMails();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the user profile's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    public String readProfileSecurityQuestion(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readProfileSecurityQuestion(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public Integer readQueueSize() {
        logger.logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readEventQueueSize(localUserId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a single user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readUser(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readUser(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#registerQueueListener()
     *
     */
    public void registerQueueListener() {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.registerQueueListener();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    public void removeListener(final SessionListener listener) {
        super.removeListener(listener);
    }

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void removeProfileEmail(final JabberId userId, final ProfileEMail email) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.removeProfileEmail(userId, email.getEmail());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#removeTeamMember(java.util.UUID,
     *      java.util.List, com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void removeTeamMember(final UUID uniqueId, final List<JabberId> team,
            final JabberId teamMemberId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("team", team);
        logger.logVariable("teamMemberId", teamMemberId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.removeTeamMember(localUserId(), team, uniqueId,
                        teamMemberId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Reset the user's authentication credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param securityAnswer
     *            A security question answer <code>String</code>.
     * @return A new password.
     */
    public String resetProfilePassword(final JabberId userId,
            final String securityAnswer) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("securityAnswer", "XXXXX");
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.resetProfilePassword(userId, securityAnswer);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#restoreArtifact(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     *
     */
    public void restoreArtifact(final JabberId userId, final UUID uniqueId) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.restoreArtifact(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#updateProfile(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.profile.Profile)
     * 
     */
    public void updateProfile(final JabberId userId, final Profile profile) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("profile", profile);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.updateProfile(userId, profile);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.session.InternalSessionModel#updateProfilePassword(java.lang.String,
     *      java.lang.String)
     * 
     */
    public void updateProfilePassword(final String password,
            final String newPassword) {
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.updateProfilePassword(localUserId(), password,
                        newPassword);
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
    public void verifyProfileEmail(final JabberId userId,
            final ProfileEMail email, final String key) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        logger.logVariable("key", key);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.verifyProfileEmail(userId, email.getEmail(), key);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
    }
}
